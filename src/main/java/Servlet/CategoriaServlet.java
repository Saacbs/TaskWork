package Servlet;

import dao.CategoriaDAO;
import model.Categoria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/categorias")
public class CategoriaServlet extends BaseServlet {
    private final CategoriaDAO dao = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        if ("excluir".equals(pedido.getParameter("acao"))) {
            dao.excluir(inteiro(pedido.getParameter("id")));
            pedido.getSession().setAttribute("sucesso", "Categoria excluída com sucesso.");
            voltar(pedido, resposta, "/categorias");
            return;
        }
        carregar(pedido, inteiro(pedido.getParameter("id")));
        abrir(pedido, resposta, "categorias.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        pedido.setCharacterEncoding("UTF-8");
        Categoria categoria = new Categoria();
        categoria.setId(inteiro(pedido.getParameter("id")));
        categoria.setNome(pedido.getParameter("nome").trim());
        categoria.setDescricao(pedido.getParameter("descricao").trim());
        categoria.setAtivo(pedido.getParameter("ativo") != null);
        try {
            if (categoria.getNome().isEmpty()) throw new IllegalArgumentException("Preencha o nome da categoria.");
            dao.salvar(categoria);
            pedido.getSession().setAttribute("sucesso", "Categoria salva com sucesso.");
            voltar(pedido, resposta, "/categorias");
        } catch (Exception e) {
            pedido.setAttribute("erro", e.getMessage());
            carregar(pedido, categoria);
            abrir(pedido, resposta, "categorias.jsp");
        }
    }

    private void carregar(HttpServletRequest pedido, int id) { carregar(pedido, id > 0 ? dao.buscarPorId(id) : new Categoria()); }
    private void carregar(HttpServletRequest pedido, Categoria categoria) {
        pedido.setAttribute("item", categoria == null ? new Categoria() : categoria);
        pedido.setAttribute("itens", dao.listar());
    }
}
