
package Servlet;

import com.google.gson.Gson;
import dao.ColaboradorDAO;
import dao.EmpresaDAO;
import model.Colaborador;
import model.Empresa;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/create-colaborador")
public class CreateColaboradorServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        EmpresaDAO empresaDAO = new EmpresaDAO();
        List<Empresa> empresas = empresaDAO.listarEmpresas();

        resposta.setContentType("application/json");
        resposta.setCharacterEncoding("UTF-8");

        resposta.getWriter().write(gson.toJson(empresas));
    }

    @Override
    protected void doPost(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        pedir.setCharacterEncoding("UTF-8");

        String nome = pedir.getParameter("nome");
        String email = pedir.getParameter("email");
        String telefone = pedir.getParameter("telefone");

        int idDaEmpresa = Integer.parseInt(
                pedir.getParameter("idDaEmpresa")
        );

        Colaborador colaborador = new Colaborador();

        colaborador.setNome(nome);
        colaborador.setEmail(email);
        colaborador.setTelefone(telefone);
        colaborador.setIdDaEmpresa(idDaEmpresa);

        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        colaboradorDAO.createColaborador(colaborador);

        resposta.sendRedirect("index.html");
    }
}
