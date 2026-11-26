package Servlet;

import dao.*;

import model.*;

import util.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/cadastro")
public class CadastroPublicoServlet extends BaseServlet {
    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        carregar(p);
        abrir(p, r, "cadastro-publico.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        try {
            SolicitacaoCadastro s = new SolicitacaoCadastro();
            s.setNome(Validacao.texto(p, "nome", 160, true));
            s.setEmail(Validacao.email(p));
            s.setTelefone(Validacao.texto(p, "telefone", 25, false));
            s.setEmpresaId(inteiro(p.getParameter("empresaId")));
            s.setPerfil(Validacao.texto(p, "perfil", 20, true));
            s.setSenha(Senha.gerar(p.getParameter("senha")));
            if (!Arrays.asList("Colaborador", "Atendente").contains(s.getPerfil()))
                throw new IllegalArgumentException("Perfil inválido.");
            Empresa empresa = new EmpresaDAO().buscarPorId(s.getEmpresaId());
            if (empresa == null || !empresa.isAtivo())
                throw new IllegalArgumentException("Selecione uma empresa ativa.");
            new SolicitacaoCadastroDAO().solicitar(s);
            p.getSession()
                    .setAttribute(
                            "sucesso",
                            "Solicitação enviada. Aguarde a aprovação do administrador.");
            voltar(p, r, "/login");
        } catch (IllegalArgumentException e) {
            p.setAttribute("erro", e.getMessage());
            carregar(p);
            abrir(p, r, "cadastro-publico.jsp");
        }
    }

    private void carregar(HttpServletRequest p) {
        List<Empresa> empresas = new EmpresaDAO().listar();
        empresas.removeIf(e -> !e.isAtivo());
        p.setAttribute("empresas", empresas);
    }
}
