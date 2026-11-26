package Servlet;

import dao.*;

import model.*;

import util.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/usuarios")
public class UsuarioServlet extends BaseServlet {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final EmpresaDAO empresas = new EmpresaDAO();

    private boolean permitido(Usuario ator, Usuario alvo) {
        return alvo != null
                && !Seguranca.master(alvo)
                && (Seguranca.master(ator)
                        || alvo.getEmpresaId() == ator.getEmpresaId()
                                && Arrays.asList("Colaborador", "Atendente")
                                        .contains(alvo.getPerfil()));
    }

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Usuario ator = Seguranca.usuario(p), item = new Usuario();
        int id = inteiro(p.getParameter("id"));
        if (id > 0) {
            item = dao.buscarPorId(id);
            if (!permitido(ator, item)) {
                r.sendError(403);
                return;
            }
        }
        carregar(p, item);
        abrir(p, r, "usuarios.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Usuario ator = Seguranca.usuario(p), u = new Usuario();
        int id = inteiro(p.getParameter("id"));
        Usuario anterior = id > 0 ? dao.buscarPorId(id) : null;
        if (id > 0 && !permitido(ator, anterior)) {
            r.sendError(403);
            return;
        }
        try {
            if ("excluir".equals(p.getParameter("acao"))) {
                if (anterior == null) throw new IllegalArgumentException("Usuário não encontrado.");
                dao.excluir(id, ator);
                sucesso(p, "Usuário excluído.");
                voltar(p, r, "/usuarios");
                return;
            }
            u.setId(id);
            u.setNome(Validacao.texto(p, "nome", 160, true));
            u.setEmail(Validacao.email(p));
            u.setTelefone(Validacao.texto(p, "telefone", 25, false));
            u.setPerfil(Validacao.texto(p, "perfil", 20, true));
            if (!Arrays.asList("Administrador", "Colaborador", "Atendente").contains(u.getPerfil())
                    || !Seguranca.master(ator) && "Administrador".equals(u.getPerfil())) {
                r.sendError(403);
                return;
            }
            int empresaId = inteiro(p.getParameter("empresaId"));
            if (!Seguranca.master(ator) && empresaId != ator.getEmpresaId()) {
                r.sendError(403);
                return;
            }
            Empresa empresa = empresas.buscarPorId(empresaId);
            if (empresa == null || !empresa.isAtivo())
                throw new IllegalArgumentException("Selecione uma empresa ativa.");
            if (anterior != null && anterior.getEmpresaId() != empresaId)
                throw new IllegalArgumentException(
                        "Não é permitido transferir usuários entre empresas. Inative o cadastro"
                                + " anterior.");
            u.setEmpresaId(empresaId);
            u.setAtivo(p.getParameter("ativo") != null);
            String senha = p.getParameter("senha");
            u.setSenha(
                    senha != null && !senha.isEmpty()
                            ? Senha.gerar(senha)
                            : anterior == null ? null : anterior.getSenha());
            if (u.getSenha() == null)
                throw new IllegalArgumentException("Informe a senha inicial do usuário.");
            dao.salvar(u, ator);
            sucesso(p, "Usuário salvo.");
            voltar(p, r, "/usuarios");
        } catch (IllegalArgumentException e) {
            p.setAttribute("erro", e.getMessage());
            carregar(p, u);
            abrir(p, r, "usuarios.jsp");
        }
    }

    private void sucesso(HttpServletRequest p, String texto) {
        p.getSession().setAttribute("sucesso", texto);
    }

    private void carregar(HttpServletRequest p, Usuario item) {
        Usuario ator = Seguranca.usuario(p);
        p.setAttribute("item", item);
        List<Usuario> lista =
                Seguranca.master(ator) ? dao.listar() : dao.listarEmpresa(ator.getEmpresaId());
        lista.removeIf(u -> !permitido(ator, u));
        p.setAttribute("itens", lista);
        List<Empresa> listaEmpresas = empresas.listar();
        listaEmpresas.removeIf(
                e -> !e.isAtivo() || !Seguranca.master(ator) && e.getId() != ator.getEmpresaId());
        p.setAttribute("empresas", listaEmpresas);
    }
}
