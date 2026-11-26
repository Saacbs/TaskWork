package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/confirmar", "/erro"})
public class TelaVisualServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta)
            throws ServletException, IOException {
        String rota = pedido.getServletPath();
        String pagina =
                "/cadastro".equals(rota) ? "cadastro-publico.jsp" : rota.substring(1) + ".jsp";
        abrir(pedido, resposta, pagina);
    }
}
