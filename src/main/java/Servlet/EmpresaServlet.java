package Servlet;

import dao.EmpresaDAO;

import model.Empresa;

import util.Validacao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/empresas")
public class EmpresaServlet extends BaseServlet {
    private final EmpresaDAO dao = new EmpresaDAO();

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        int id = inteiro(p.getParameter("id"));
        Empresa e = id > 0 ? dao.buscarPorId(id) : new Empresa();
        if (e == null) {
            r.sendError(404);
            return;
        }
        carregar(p, e);
        abrir(p, r, "empresas.jsp");
    }

    protected void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Empresa e = new Empresa();
        e.setId(inteiro(p.getParameter("id")));
        try {
            if ("excluir".equals(p.getParameter("acao"))) {
                if (!dao.excluir(e.getId()))
                    throw new IllegalArgumentException(
                            "Empresa possui usuários. Inative o cadastro.");
                p.getSession().setAttribute("sucesso", "Empresa excluída.");
                voltar(p, r, "/empresas");
                return;
            }
            e.setNome(Validacao.texto(p, "nome", 160, true));
            String cnpj = Validacao.texto(p, "cnpj", 18, true).replaceAll("[^0-9]", "");
            if (!cnpj.matches("[0-9]{14}"))
                throw new IllegalArgumentException("O CNPJ deve conter 14 dígitos.");
            e.setCnpj(cnpj);
            e.setAtivo(p.getParameter("ativo") != null);
            if (e.getId() > 0 && dao.buscarPorId(e.getId()) == null)
                throw new IllegalArgumentException("Empresa não encontrada.");
            dao.salvar(e);
            p.getSession().setAttribute("sucesso", "Empresa salva.");
            voltar(p, r, "/empresas");
        } catch (Exception ex) {
            p.setAttribute(
                    "erro",
                    ex instanceof IllegalArgumentException
                            ? ex.getMessage()
                            : "Não foi possível salvar. Confira o CNPJ e os vínculos da empresa.");
            carregar(p, e);
            abrir(p, r, "empresas.jsp");
        }
    }

    private void carregar(HttpServletRequest p, Empresa e) {
        p.setAttribute("item", e);
        p.setAttribute("itens", dao.listar());
    }
}
