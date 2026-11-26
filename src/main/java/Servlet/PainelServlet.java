package Servlet;

import dao.ChamadoDAO;

import model.*;

import util.Seguranca;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/painel")
public class PainelServlet extends BaseServlet {
    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        List<Chamado> lista = new ChamadoDAO().listar(Seguranca.usuario(p));
        p.setAttribute("resumo", new Resumo(lista));
        p.setAttribute("itens", lista.subList(0, Math.min(10, lista.size())));
        abrir(p, r, "painel.jsp");
    }
}
