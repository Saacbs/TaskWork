package Servlet;

import dao.NotificacaoDAO;

import util.Seguranca;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/notificacoes")
public class NotificacaoServlet extends BaseServlet {
    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        p.setAttribute("itens", new NotificacaoDAO().listar(Seguranca.usuario(p)));
        abrir(p, r, "notificacoes.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r) throws IOException {
        if ("todas".equals(p.getParameter("acao")))
            new NotificacaoDAO().lerTodas(Seguranca.usuario(p).getId());
        else new NotificacaoDAO().ler(inteiro(p.getParameter("id")), Seguranca.usuario(p).getId());
        voltar(p, r, "/notificacoes");
    }
}
