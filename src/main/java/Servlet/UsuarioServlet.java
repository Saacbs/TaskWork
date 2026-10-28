package Servlet;

import dao.EmpresaDAO;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/usuarios")
public class UsuarioServlet extends BaseServlet {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final EmpresaDAO empresaDAO = new EmpresaDAO();

    @Override
    protected void doGet(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        if ("excluir".equals(pedido.getParameter("acao"))) {
            dao.excluir(inteiro(pedido.getParameter("id")));
            pedido.getSession().setAttribute("sucesso", "Usuário excluído com sucesso.");
            voltar(pedido, resposta, "/usuarios");
            return;
        }
        carregar(pedido, inteiro(pedido.getParameter("id")));
        abrir(pedido, resposta, "usuarios.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest pedido, HttpServletResponse resposta) throws ServletException, IOException {
        pedido.setCharacterEncoding("UTF-8");
        Usuario usuario = new Usuario();
        usuario.setId(inteiro(pedido.getParameter("id")));
        usuario.setNome(pedido.getParameter("nome").trim());
        usuario.setEmail(pedido.getParameter("email").trim());
        usuario.setTelefone(pedido.getParameter("telefone").trim());
        usuario.setEmpresaId(inteiro(pedido.getParameter("empresaId")));
        usuario.setPerfil(pedido.getParameter("perfil"));
        usuario.setAtivo(pedido.getParameter("ativo") != null);
        try {
            if (usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getEmpresaId() == 0) {
                throw new IllegalArgumentException("Preencha nome, e-mail e empresa.");
            }
            if (!Arrays.asList("Administrador", "Colaborador", "Atendente").contains(usuario.getPerfil())) {
                throw new IllegalArgumentException("Perfil inválido.");
            }
            dao.salvar(usuario);
            pedido.getSession().setAttribute("sucesso", "Usuário salvo com sucesso.");
            voltar(pedido, resposta, "/usuarios");
        } catch (Exception e) {
            pedido.setAttribute("erro", e.getMessage());
            carregar(pedido, usuario);
            abrir(pedido, resposta, "usuarios.jsp");
        }
    }

    private void carregar(HttpServletRequest pedido, int id) { carregar(pedido, id > 0 ? dao.buscarPorId(id) : new Usuario()); }
    private void carregar(HttpServletRequest pedido, Usuario usuario) {
        pedido.setAttribute("item", usuario == null ? new Usuario() : usuario);
        pedido.setAttribute("itens", dao.listar());
        pedido.setAttribute("empresas", empresaDAO.listar());
    }
}
