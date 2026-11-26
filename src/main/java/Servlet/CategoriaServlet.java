package Servlet;

import dao.CategoriaDAO;

import model.Categoria;

import util.Validacao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/categorias")
public class CategoriaServlet extends BaseServlet {
    private final CategoriaDAO dao = new CategoriaDAO();

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        int id = inteiro(p.getParameter("id"));
        Categoria c = id > 0 ? dao.buscarPorId(id) : new Categoria();
        if (c == null) {
            r.sendError(404);
            return;
        }
        carregar(p, c);
        abrir(p, r, "categorias.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Categoria c = new Categoria();
        c.setId(inteiro(p.getParameter("id")));
        try {
            if ("excluir".equals(p.getParameter("acao"))) {
                dao.excluir(c.getId());
                p.getSession().setAttribute("sucesso", "Categoria excluída.");
                voltar(p, r, "/categorias");
                return;
            }
            c.setNome(Validacao.texto(p, "nome", 160, true));
            c.setDescricao(Validacao.texto(p, "descricao", 500, false));
            c.setAtivo(p.getParameter("ativo") != null);
            if (c.getId() > 0 && dao.buscarPorId(c.getId()) == null)
                throw new IllegalArgumentException("Categoria não encontrada.");
            dao.salvar(c);
            p.getSession().setAttribute("sucesso", "Categoria salva.");
            voltar(p, r, "/categorias");
        } catch (Exception ex) {
            p.setAttribute(
                    "erro",
                    ex instanceof IllegalArgumentException
                            ? ex.getMessage()
                            : "Categoria não pôde ser alterada/excluída. Se houver chamados"
                                    + " vinculados, inative o cadastro.");
            carregar(p, c);
            abrir(p, r, "categorias.jsp");
        }
    }

    private void carregar(HttpServletRequest p, Categoria c) {
        p.setAttribute("item", c);
        p.setAttribute("itens", dao.listar());
    }
}
