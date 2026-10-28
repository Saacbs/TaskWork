package Servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected int inteiro(String valor) {
        try { return Integer.parseInt(valor); } catch (Exception e) { return 0; }
    }

    protected void abrir(HttpServletRequest pedido, HttpServletResponse resposta, String pagina)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = pedido.getRequestDispatcher("/WEB-INF/views/" + pagina);
        dispatcher.forward(pedido, resposta);
    }

    protected void voltar(HttpServletRequest pedido, HttpServletResponse resposta, String rota)
            throws IOException {
        resposta.sendRedirect(pedido.getContextPath() + rota);
    }
}
