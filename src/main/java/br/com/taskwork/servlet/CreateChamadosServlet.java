package br.com.taskwork.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create-chamados")
public class CreateChamadosServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        String nomecolab = pedir.getParameter("nome");
        String emailcolab = pedir.getParameter("email");
        String numero = pedir.getParameter("numero");

        System.out.println(nomecolab);
        System.out.println(emailcolab);
        System.out.println(numero);

        resposta.sendRedirect("index.html");
    }
}
