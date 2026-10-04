package Servlet;

import dao.EmpresaDAO;
import dao.ColaboradorDAO;
import model.Empresa;
import model.Colaborador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/create-colaborador")
public class CreateColaboradorServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        EmpresaDAO empresaDAO = new EmpresaDAO();

        List<Empresa> empresas = empresaDAO.listarEmpresas();

        pedir.setAttribute("empresas", empresas);

        pedir.getRequestDispatcher("CadastroColaborador.jsp").forward(pedir, resposta);
    }

    @Override
    protected void doPost(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        String nome = pedir.getParameter("nome");
        String email = pedir.getParameter("email");
        String telefone = pedir.getParameter("telefone");

        int idDaEmpresa = Integer.parseInt(pedir.getParameter("idDaEmpresa"));

        Colaborador colaborador = new Colaborador();

        colaborador.setNome(nome);
        colaborador.setEmail(email);
        colaborador.setTelefone(telefone);
        colaborador.setIdDaEmpresa(idDaEmpresa);

        new ColaboradorDAO().createColaborador(colaborador);

        resposta.sendRedirect("index.html");
    }
}