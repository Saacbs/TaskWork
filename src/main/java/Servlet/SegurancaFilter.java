package Servlet;

import dao.*;

import model.*;

import util.Seguranca;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class SegurancaFilter implements Filter {
    public void init(FilterConfig config) {}

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest p = (HttpServletRequest) req;
        HttpServletResponse r = (HttpServletResponse) res;
        p.setCharacterEncoding("UTF-8");
        r.setCharacterEncoding("UTF-8");
        r.setHeader("X-Content-Type-Options", "nosniff");
        r.setHeader("X-Frame-Options", "DENY");
        String rota = p.getServletPath();
        if (rota.startsWith("/css/") || rota.startsWith("/js/") || rota.startsWith("/Imagens/")) {
            chain.doFilter(p, r);
            return;
        }
        r.setHeader("Cache-Control", "no-store");
        HttpSession sessao = p.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioId") instanceof Integer) {
            Usuario u = new UsuarioDAO().buscarPorId((Integer) sessao.getAttribute("usuarioId"));
            Empresa empresa = u == null ? null : new EmpresaDAO().buscarPorId(u.getEmpresaId());
            if (u != null
                    && Seguranca.versao(u, empresa).equals(sessao.getAttribute("versao"))
                    && u.isAtivo()
                    && (Seguranca.master(u) || empresa != null && empresa.isAtivo()))
                p.setAttribute("usuarioLogado", u);
            else sessao.invalidate();
        }
        boolean publica =
                Arrays.asList(
                                "/",
                                "",
                                "/index.html",
                                "/inicio",
                                "/login",
                                "/cadastro",
                                "/configuracao",
                                "/confirmar")
                        .contains(rota);
        Usuario u = Seguranca.usuario(p);
        if (!publica && u == null) {
            if ("GET".equals(p.getMethod()) && "/chamado-detalhes".equals(rota)) {
                int id = util.Validacao.protocolo(p.getParameter("id"));
                if (id > 0) p.getSession().setAttribute("destino", "/chamado-detalhes?id=" + id);
            }
            r.sendRedirect(p.getContextPath() + "/login");
            return;
        }
        if (("/empresas".equals(rota) && !Seguranca.master(u))
                || (Arrays.asList("/usuarios", "/categorias", "/solicitacoes").contains(rota)
                        && !Seguranca.administrador(u))
                || ("/relatorios".equals(rota) && !Seguranca.equipe(u))) {
            r.sendError(403);
            return;
        }
        if ("POST".equals(p.getMethod())) {
            // O multipart recebe o token na URL para validar antes de ler os arquivos.
            String enviado = p.getParameter("csrf");
            if (enviado == null || !Seguranca.csrf(p).equals(enviado)) {
                r.sendError(403, "Formulário expirado. Atualize a página.");
                return;
            }
        }
        Seguranca.csrf(p);
        chain.doFilter(p, r);
    }
}
