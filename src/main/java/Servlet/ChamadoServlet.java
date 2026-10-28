package Servlet;

import dao.ChamadoDAO;
import model.Chamado;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/chamados")
public class ChamadoServlet extends BaseServlet {
    private final ChamadoDAO dao = new ChamadoDAO();

    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        String acao = pedido.getParameter("acao");
        if ("excluir".equals(acao)) {
            dao.excluir(inteiro(pedido.getParameter("id")));
            pedido.getSession().setAttribute("sucesso", "Chamado excluído com sucesso.");
            voltar(pedido, resposta, "/chamados");
            return;
        }
        if ("novo".equals(acao) || "editar".equals(acao)) {
            carregar(pedido, "editar".equals(acao) ? inteiro(pedido.getParameter("id")) : 0);
            abrir(pedido, resposta, "chamado-form.jsp");
            return;
        }
        carregar(pedido, inteiro(pedido.getParameter("id")));
        abrir(pedido, resposta, "chamados.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        pedido.setCharacterEncoding("UTF-8");
        Chamado chamado = new Chamado();
        chamado.setId(inteiro(pedido.getParameter("id")));
        chamado.setTitulo(pedido.getParameter("titulo").trim());
        chamado.setDescricao(pedido.getParameter("descricao").trim());
        chamado.setSetor(pedido.getParameter("setor").trim());
        chamado.setCategoria(pedido.getParameter("categoria").trim());
        chamado.setPrioridade(pedido.getParameter("prioridade"));
        chamado.setStatus(pedido.getParameter("status"));
        try {
            if (chamado.getTitulo().isEmpty() || chamado.getDescricao().isEmpty()
                    || chamado.getSetor().isEmpty() || chamado.getCategoria().isEmpty()) {
                throw new IllegalArgumentException("Preencha todos os campos obrigatórios.");
            }
            if (!Arrays.asList("Baixa", "Média", "Alta", "Crítica").contains(chamado.getPrioridade())
                    || !Arrays.asList("Aberto", "Em atendimento", "Resolvido").contains(chamado.getStatus())) {
                throw new IllegalArgumentException("Prioridade ou status inválido.");
            }
            dao.salvar(chamado);
            pedido.getSession().setAttribute("sucesso", "Chamado salvo com sucesso.");
            voltar(pedido, resposta, "/chamados");
        } catch (Exception e) {
            pedido.setAttribute("erro", e.getMessage());
            carregar(pedido, chamado);
            abrir(pedido, resposta, "chamado-form.jsp");
        }
    }

    private void carregar(HttpServletRequest pedido, int id) { carregar(pedido, id > 0 ? dao.buscarPorId(id) : new Chamado()); }
    private void carregar(HttpServletRequest pedido, Chamado chamado) {
        pedido.setAttribute("item", chamado == null ? new Chamado() : chamado);
        pedido.setAttribute("itens", dao.listar());
    }
}
