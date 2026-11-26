package util;

import model.Usuario;

import javax.servlet.http.HttpServletRequest;

public class Seguranca {
    public static boolean podeReabrir(Usuario u, model.Chamado c, java.sql.Timestamp agora) {
        return podeVer(u, c)
                && c.isEncerrado()
                && (administrador(u)
                        || c.getSolicitanteId() == u.getId()
                                && "Resolvido".equals(c.getStatus())
                                && c.getResolvidoEm() != null
                                && !agora.before(c.getResolvidoEm())
                                && agora.getTime() - c.getResolvidoEm().getTime()
                                        <= java.util.concurrent.TimeUnit.DAYS.toMillis(7));
    }

    public static String versao(Usuario u, model.Empresa e) {
        return u.getVersaoSessao() + ":" + (e == null ? 0 : e.getVersaoSessao());
    }

    public static boolean podeVer(Usuario u, model.Chamado c) {
        return u != null
                && c != null
                && (master(u)
                        || c.getEmpresaId() > 0
                                && c.getEmpresaId() == u.getEmpresaId()
                                && (equipe(u) || c.getSolicitanteId() == u.getId()));
    }

    public static Usuario usuario(HttpServletRequest p) {
        return (Usuario) p.getAttribute("usuarioLogado");
    }

    public static boolean master(Usuario u) {
        return u != null && "Administrador master".equals(u.getPerfil());
    }

    public static boolean administrador(Usuario u) {
        return master(u) || u != null && "Administrador".equals(u.getPerfil());
    }

    public static boolean equipe(Usuario u) {
        return administrador(u) || u != null && "Atendente".equals(u.getPerfil());
    }

    public static String csrf(HttpServletRequest p) {
        Object token = p.getSession().getAttribute("csrf");
        if (token == null) {
            token = java.util.UUID.randomUUID().toString();
            p.getSession().setAttribute("csrf", token);
        }
        return token.toString();
    }

    public static String campoCsrf(HttpServletRequest p) {
        return "<input type=\"hidden\" name=\"csrf\" value=\"" + csrf(p) + "\">";
    }
}
