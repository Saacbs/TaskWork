package Servlet;

import dao.SolicitacaoCadastroDAO;

import util.Seguranca;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/solicitacoes")
public class SolicitacaoCadastroServlet extends BaseServlet {
    private final SolicitacaoCadastroDAO dao = new SolicitacaoCadastroDAO();

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        p.setAttribute("itens", dao.listar(Seguranca.usuario(p)));
        abrir(p, r, "solicitacoes.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r) throws IOException {
        try {
            String acao = p.getParameter("acao");
            if (!"aprovar".equals(acao) && !"rejeitar".equals(acao))
                throw new IllegalArgumentException("Ação inválida.");
            dao.decidir(
                    inteiro(p.getParameter("id")), "aprovar".equals(acao), Seguranca.usuario(p));
            p.getSession().setAttribute("sucesso", "Solicitação analisada.");
        } catch (IllegalArgumentException e) {
            p.getSession().setAttribute("erro", e.getMessage());
        }
        voltar(p, r, "/solicitacoes");
    }
}
