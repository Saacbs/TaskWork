package Servlet;

import dao.*;

import model.*;

import util.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/login", "/sair", "/configuracao"})
public class LoginServlet extends BaseServlet {
    private final UsuarioDAO dao = new UsuarioDAO();

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        if ("/sair".equals(p.getServletPath())) {
            r.sendError(405);
            return;
        }
        if ("/configuracao".equals(p.getServletPath()) && dao.temMaster()) {
            r.sendError(404);
            return;
        }
        abrir(p, r, "/configuracao".equals(p.getServletPath()) ? "configuracao.jsp" : "login.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        if ("/sair".equals(p.getServletPath())) {
            p.getSession().invalidate();
            voltar(p, r, "/login");
            return;
        }
        try {
            if ("/configuracao".equals(p.getServletPath())) {
                synchronized (LoginServlet.class) {
                    if (dao.temMaster() || !Banco.CHAVE_INICIAL.equals(p.getParameter("chave"))) {
                        r.sendError(403, "Configuração indisponível ou chave inválida.");
                        return;
                    }
                    Usuario u = new Usuario();
                    u.setNome(Validacao.texto(p, "nome", 160, true));
                    u.setEmail(Validacao.email(p));
                    u.setPerfil("Administrador master");
                    u.setSenha(Senha.gerar(p.getParameter("senha")));
                    dao.salvar(u);
                }
                p.getSession()
                        .setAttribute(
                                "sucesso", "Administrador master criado. Entre com seus dados.");
                voltar(p, r, "/login");
                return;
            }
            Usuario u = dao.buscarEmail(Validacao.email(p));
            Empresa empresa = u == null ? null : new EmpresaDAO().buscarPorId(u.getEmpresaId());
            if (u == null
                    || !u.isAtivo()
                    || !Senha.conferir(p.getParameter("senha"), u.getSenha())
                    || !Seguranca.master(u) && (empresa == null || !empresa.isAtivo()))
                throw new IllegalArgumentException(
                        "E-mail ou senha inválidos, ou acesso não liberado.");
            Object destino = p.getSession().getAttribute("destino");
            p.getSession().invalidate();
            HttpSession sessao = p.getSession(true);
            sessao.setAttribute("usuarioId", u.getId());
            sessao.setAttribute("versao", Seguranca.versao(u, empresa));
            sessao.setMaxInactiveInterval(1800);
            voltar(
                    p,
                    r,
                    destino instanceof String
                                    && ((String) destino).matches("/chamado-detalhes\\?id=[0-9]+")
                            ? (String) destino
                            : "/painel");
        } catch (IllegalArgumentException e) {
            p.setAttribute("erro", e.getMessage());
            abrir(
                    p,
                    r,
                    "/configuracao".equals(p.getServletPath()) ? "configuracao.jsp" : "login.jsp");
        }
    }
}
