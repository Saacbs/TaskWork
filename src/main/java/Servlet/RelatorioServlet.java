package Servlet;

import dao.ChamadoDAO;

import model.*;

import util.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/relatorios")
public class RelatorioServlet extends BaseServlet {
    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        List<Chamado> lista = new ChamadoDAO().listar(Seguranca.usuario(p));
        try {
            String inicio = Validacao.texto(p, "inicio", 10, false),
                    fim = Validacao.texto(p, "fim", 10, false),
                    status = Validacao.texto(p, "status", 30, false);
            LocalDate de = inicio.isEmpty() ? null : LocalDate.parse(inicio),
                    ate = fim.isEmpty() ? null : LocalDate.parse(fim);
            if (de != null && ate != null && de.isAfter(ate))
                throw new IllegalArgumentException(
                        "A data inicial deve ser anterior ou igual à final.");
            int categoria = inteiro(p.getParameter("categoriaId")),
                    responsavel = inteiro(p.getParameter("responsavelId"));
            lista.removeIf(
                    c ->
                            !status.isEmpty() && !status.equals(c.getStatus())
                                    || categoria > 0 && categoria != c.getCategoriaId()
                                    || responsavel > 0 && responsavel != c.getResponsavelId()
                                    || de != null
                                            && (c.getCriadoEm() == null
                                                    || c.getCriadoEm()
                                                            .toLocalDateTime()
                                                            .toLocalDate()
                                                            .isBefore(de))
                                    || ate != null
                                            && (c.getCriadoEm() == null
                                                    || c.getCriadoEm()
                                                            .toLocalDateTime()
                                                            .toLocalDate()
                                                            .isAfter(ate)));
        } catch (java.time.format.DateTimeParseException e) {
            p.setAttribute("erro", "Informe datas válidas.");
            lista.clear();
        } catch (IllegalArgumentException e) {
            p.setAttribute("erro", e.getMessage());
            lista.clear();
        }
        p.setAttribute("categorias", new dao.CategoriaDAO().listar());
        model.Usuario ator = Seguranca.usuario(p);
        List<model.Usuario> equipe =
                Seguranca.master(ator)
                        ? new dao.UsuarioDAO().listar()
                        : new dao.UsuarioDAO().listarEmpresa(ator.getEmpresaId());
        equipe.removeIf(u -> !Seguranca.equipe(u));
        p.setAttribute("atendentes", equipe);
        p.setAttribute("resumo", new Resumo(lista));
        p.setAttribute("itens", lista);
        abrir(p, r, "relatorios.jsp");
    }
}
