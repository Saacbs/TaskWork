package Servlet;

import dao.*;

import model.*;

import util.Seguranca;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/anexos")
public class AnexoServlet extends BaseServlet {
    protected void doPost(HttpServletRequest p, HttpServletResponse r) throws IOException {
        Anexo a = new AnexoDAO().buscar(inteiro(p.getParameter("id")));
        model.Usuario ator = Seguranca.usuario(p);
        if (a == null || new ChamadoDAO().buscarPorId(a.getChamadoId(), ator) == null) {
            r.sendError(404);
            return;
        }
        if (!"excluir".equals(p.getParameter("acao"))) {
            r.sendError(400);
            return;
        }
        if (a.getAutorId() != ator.getId() && !Seguranca.administrador(ator)) {
            r.sendError(403);
            return;
        }
        try {
            new AnexoDAO().excluir(a, ator);
            p.getSession().setAttribute("sucesso", "Anexo excluído.");
        } catch (IllegalArgumentException e) {
            p.getSession().setAttribute("erro", e.getMessage());
        }
        voltar(p, r, "/chamado-detalhes?id=" + a.getChamadoId());
    }

    protected void doGet(HttpServletRequest p, HttpServletResponse r) throws IOException {
        Anexo a = new AnexoDAO().buscar(inteiro(p.getParameter("id")));
        if (a == null
                || new ChamadoDAO().buscarPorId(a.getChamadoId(), Seguranca.usuario(p)) == null) {
            r.sendError(404);
            return;
        }
        r.setContentType(a.getTipo());
        r.setContentLength(a.getConteudo().length);
        r.setHeader(
                "Content-Disposition",
                "attachment; filename=\"anexo\"; filename*=UTF-8''"
                        + URLEncoder.encode(a.getNome(), StandardCharsets.UTF_8)
                                .replace("+", "%20"));
        r.getOutputStream().write(a.getConteudo());
    }
}
