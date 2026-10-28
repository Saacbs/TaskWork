package Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/inicio")
public class InicioServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta) throws IOException {
        try {
            abrir(pedido, resposta, "inicio.jsp");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
