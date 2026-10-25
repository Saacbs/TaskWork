
package Servlet;

import com.google.gson.Gson;
import dao.ColaboradorDAO;
import model.Colaborador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/listar-colaborador")
public class ListarColaboradoresServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest pedir,
            HttpServletResponse resposta
    ) throws ServletException, IOException {

        ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
        List<Colaborador> colaboradores = colaboradorDAO.listarColaboradores();

        resposta.setContentType("application/json");
        resposta.setCharacterEncoding("UTF-8");

        resposta.getWriter().write(new Gson().toJson(colaboradores));
    }
}
