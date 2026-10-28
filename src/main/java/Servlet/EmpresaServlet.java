package Servlet;

import dao.EmpresaDAO;
import model.Empresa;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/empresas")
public class EmpresaServlet extends BaseServlet {
    private final EmpresaDAO dao = new EmpresaDAO();

    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        if ("excluir".equals(pedido.getParameter("acao"))) {
            if (dao.excluir(inteiro(pedido.getParameter("id")))) {
                pedido.getSession().setAttribute("sucesso", "Empresa excluída com sucesso.");
            } else {
                pedido.getSession().setAttribute("erro", "Exclua os usuários da empresa antes de excluir a empresa.");
            }
            voltar(pedido, resposta, "/empresas");
            return;
        }
        carregar(pedido, inteiro(pedido.getParameter("id")));
        abrir(pedido, resposta, "empresas.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        pedido.setCharacterEncoding("UTF-8");
        Empresa empresa = new Empresa();
        empresa.setId(inteiro(pedido.getParameter("id")));
        empresa.setNome(pedido.getParameter("nome").trim());
        empresa.setCnpj(pedido.getParameter("cnpj").trim());
        empresa.setAtivo(pedido.getParameter("ativo") != null);
        try {
            if (empresa.getNome().isEmpty() || empresa.getCnpj().isEmpty()) throw new IllegalArgumentException("Preencha nome e CNPJ.");
            dao.salvar(empresa);
            pedido.getSession().setAttribute("sucesso", "Empresa salva com sucesso.");
            voltar(pedido, resposta, "/empresas");
        } catch (Exception e) {
            pedido.setAttribute("erro", e.getMessage());
            carregar(pedido, empresa);
            abrir(pedido, resposta, "empresas.jsp");
        }
    }

    private void carregar(HttpServletRequest pedido, int id) { carregar(pedido, id > 0 ? dao.buscarPorId(id) : new Empresa()); }
    private void carregar(HttpServletRequest pedido, Empresa empresa) {
        pedido.setAttribute("item", empresa == null ? new Empresa() : empresa);
        pedido.setAttribute("itens", dao.listar());
    }
}
