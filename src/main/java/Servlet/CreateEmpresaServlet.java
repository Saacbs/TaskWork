package Servlet;

import dao.EmpresaDAO;
import model.Empresa;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create-empresa")
public class CreateEmpresaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        String nome = pedir.getParameter("nome");
        String cnpj = pedir.getParameter("cnpj");

        Empresa empresa = new Empresa();
        empresa.setName(nome);
        empresa.setCnpj(cnpj);

        new EmpresaDAO().createEmpresa(empresa);

        resposta.sendRedirect("index.html");
    }
}