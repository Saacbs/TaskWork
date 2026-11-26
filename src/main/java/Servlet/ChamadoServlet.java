package Servlet;

import dao.*;

import model.*;

import util.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/chamados", "/chamado-detalhes"})
@MultipartConfig(maxFileSize = 10485760, maxRequestSize = 22020096, fileSizeThreshold = 1048576)
public class ChamadoServlet extends BaseServlet {
    private final ChamadoDAO dao = new ChamadoDAO();

    protected void doGet(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Usuario ator = Seguranca.usuario(p);
        String acao = p.getParameter("acao");
        int id = Validacao.protocolo(p.getParameter("id"));
        if ("/chamado-detalhes".equals(p.getServletPath())) {
            Chamado c = dao.buscarPorId(id, ator);
            if (c == null) {
                r.sendError(404);
                return;
            }
            detalhes(p, c);
            abrir(p, r, "chamado-detalhes.jsp");
            return;
        }
        if ("novo".equals(acao) || "editar".equals(acao)) {
            Chamado c = "editar".equals(acao) ? dao.buscarPorId(id, ator) : new Chamado();
            if (c == null || c.getId() > 0 && !podeEditar(ator, c)) {
                r.sendError(403);
                return;
            }
            formulario(p, c);
            abrir(p, r, "chamado-form.jsp");
            return;
        }
        listar(p);
        abrir(p, r, "chamados.jsp");
    }

    protected synchronized void doPost(HttpServletRequest p, HttpServletResponse r)
            throws ServletException, IOException {
        Usuario ator = Seguranca.usuario(p);
        Chamado c = null;
        try {
            String idInformado = p.getParameter("id");
            if (idInformado != null
                    && (!idInformado.matches("[0-9]+")
                            || !"0".equals(idInformado) && inteiro(idInformado) <= 0)) {
                r.sendError(400, "ID de chamado inválido.");
                return;
            }
            int id = inteiro(p.getParameter("id"));
            String acao = p.getParameter("acao");
            c = id > 0 ? dao.buscarPorId(id, ator) : new Chamado();
            if (c == null) {
                r.sendError(404);
                return;
            }
            String anterior = c.getStatus(), evento;
            boolean interno = false;
            List<Anexo> anexos = new ArrayList<>();
            if (acao == null || "salvar".equals(acao)) {
                if (id > 0 && !podeEditar(ator, c)) {
                    r.sendError(403);
                    return;
                }
                c.setTitulo(Validacao.texto(p, "titulo", 160, true));
                c.setDescricao(Validacao.texto(p, "descricao", 5000, true));
                c.setSetor(Validacao.texto(p, "setor", 100, true));
                c.setPrioridade(Validacao.texto(p, "prioridade", 20, true));
                if (!Arrays.asList("Baixa", "Média", "Alta", "Crítica").contains(c.getPrioridade()))
                    throw new IllegalArgumentException("Prioridade inválida.");
                Categoria categoria =
                        new CategoriaDAO().buscarPorId(inteiro(p.getParameter("categoriaId")));
                if (categoria == null || !categoria.isAtivo())
                    throw new IllegalArgumentException("Selecione uma categoria ativa.");
                c.setCategoriaId(categoria.getId());
                c.setCategoria(categoria.getNome());
                if (id == 0) {
                    Usuario solicitante =
                            Seguranca.master(ator)
                                    ? new UsuarioDAO()
                                            .buscarPorId(inteiro(p.getParameter("solicitanteId")))
                                    : ator;
                    validarSolicitante(solicitante);
                    c.setSolicitanteId(solicitante.getId());
                    c.setEmpresaId(solicitante.getEmpresaId());
                    c.setCriadoEm(new Timestamp(System.currentTimeMillis()));
                    c.setStatus("Aberto");
                }
                if (c.getCriadoEm() != null)
                    c.setPrazoSla(
                            Timestamp.from(
                                    c.getCriadoEm()
                                            .toInstant()
                                            .plus(
                                                    java.time.Duration.ofHours(
                                                            Chamado.horasSla(c.getPrioridade())))));
                anexos = lerAnexos(p);
                evento = id == 0 ? "Chamado aberto." : "Dados do chamado atualizados.";
            } else {
                if (id == 0) throw new IllegalArgumentException("Informe o chamado.");
                if ("excluir".equals(acao)) {
                    if (!Seguranca.administrador(ator)
                            && !(c.getSolicitanteId() == ator.getId()
                                    && "Aberto".equals(c.getStatus()))) {
                        r.sendError(403);
                        return;
                    }
                    dao.excluir(id);
                    p.getSession().setAttribute("sucesso", "Chamado excluído.");
                    voltar(p, r, "/chamados");
                    return;
                }
                if ("comentar".equals(acao)) {
                    interno = p.getParameter("interno") != null;
                    if (interno && !Seguranca.equipe(ator)) {
                        r.sendError(403);
                        return;
                    }
                    evento = Validacao.texto(p, "texto", 5000, true);
                } else if ("assumir".equals(acao)) {
                    if (!Seguranca.equipe(ator)
                            || !"Aberto".equals(c.getStatus())
                            || c.getResponsavelId() > 0)
                        throw new IllegalArgumentException(
                                "Este chamado não está disponível para assumir.");
                    c.setResponsavelId(ator.getId());
                    c.setStatus("Em atendimento");
                    evento = "Atendimento assumido.";
                } else if ("atribuir".equals(acao)) {
                    if (!Seguranca.administrador(ator)) {
                        r.sendError(403);
                        return;
                    }
                    if (c.isEncerrado()) throw new IllegalArgumentException("Chamado encerrado.");
                    Usuario responsavel =
                            new UsuarioDAO().buscarPorId(inteiro(p.getParameter("responsavelId")));
                    if (responsavel == null
                            || !responsavel.isAtivo()
                            || responsavel.getEmpresaId() != c.getEmpresaId()
                            || !"Atendente".equals(responsavel.getPerfil()))
                        throw new IllegalArgumentException(
                                "Selecione um atendente ativo da empresa do chamado.");
                    c.setResponsavelId(responsavel.getId());
                    c.setStatus("Em atendimento");
                    evento = "Atendimento atribuído a " + responsavel.getNome() + ".";
                } else if ("resolver".equals(acao)) {
                    if (!Seguranca.equipe(ator)
                            || !Seguranca.administrador(ator)
                                    && c.getResponsavelId() != ator.getId()) {
                        r.sendError(403);
                        return;
                    }
                    if (!"Em atendimento".equals(c.getStatus()) || c.getResponsavelId() == 0)
                        throw new IllegalArgumentException(
                                "Assuma ou atribua o atendimento antes de resolver.");
                    c.setSolucao(Validacao.texto(p, "solucao", 5000, true));
                    c.setStatus("Resolvido");
                    c.setResolvidoEm(new Timestamp(System.currentTimeMillis()));
                    evento = "Chamado resolvido. Consulte a solução nos detalhes.";
                } else if ("cancelar".equals(acao)) {
                    if (!Seguranca.equipe(ator)
                            && !(c.getSolicitanteId() == ator.getId()
                                    && "Aberto".equals(c.getStatus()))) {
                        r.sendError(403);
                        return;
                    }
                    if (c.isEncerrado())
                        throw new IllegalArgumentException("Chamado já encerrado.");
                    evento = "Chamado cancelado: " + Validacao.texto(p, "motivo", 4800, true);
                    c.setStatus("Cancelado");
                } else if ("status".equals(acao)) {
                    if (!Seguranca.equipe(ator)) {
                        r.sendError(403);
                        return;
                    }
                    String novo = Validacao.texto(p, "status", 30, true);
                    if (c.isEncerrado()
                            || !Arrays.asList(
                                            "Aberto",
                                            "Em atendimento",
                                            "Aguardando contato",
                                            "Aguardando aprovação",
                                            "Recusado")
                                    .contains(novo)
                            || novo.equals(c.getStatus()))
                        throw new IllegalArgumentException("Status inválido para este chamado.");
                    if ("Em atendimento".equals(novo) && c.getResponsavelId() == 0)
                        throw new IllegalArgumentException(
                                "Assuma ou atribua o atendimento antes de iniciar.");
                    evento = "Status alterado para " + novo + ".";
                    if ("Recusado".equals(novo)) {
                        evento = "Chamado recusado: " + Validacao.texto(p, "motivo", 4800, true);
                        c.setResolvidoEm(new Timestamp(System.currentTimeMillis()));
                    }
                    c.setStatus(novo);
                } else if ("reabrir".equals(acao)) {
                    if (!Seguranca.podeReabrir(ator, c, new Timestamp(System.currentTimeMillis())))
                        throw new IllegalArgumentException(
                                "O solicitante pode reabrir chamados resolvidos há até 7 dias."
                                        + " Demais encerrados exigem administrador.");
                    c.setStatus("Aberto");
                    c.setResolvidoEm(null);
                    c.setSolucao(null);
                    c.setAvaliacao(0);
                    c.setComentarioAvaliacao(null);
                    c.setResponsavelId(0);
                    evento = "Chamado reaberto. Prazo de SLA original mantido.";
                } else if ("remover-avaliacao".equals(acao)) {
                    if (c.getSolicitanteId() != ator.getId()
                            || !"Resolvido".equals(c.getStatus())) {
                        r.sendError(403);
                        return;
                    }
                    c.setAvaliacao(0);
                    c.setComentarioAvaliacao(null);
                    evento = "Avaliação removida.";
                } else if ("avaliar".equals(acao)) {
                    int nota = inteiro(p.getParameter("avaliacao"));
                    if (c.getSolicitanteId() != ator.getId()
                            || !"Resolvido".equals(c.getStatus())
                            || nota < 1
                            || nota > 5)
                        throw new IllegalArgumentException(
                                "Somente o solicitante pode avaliar após a resolução (1 a"
                                        + " 5).");
                    c.setAvaliacao(nota);
                    c.setComentarioAvaliacao(
                            Validacao.texto(p, "comentarioAvaliacao", 1000, false));
                    evento = "Atendimento avaliado com nota " + nota + ".";
                } else if ("regularizar".equals(acao)) {
                    if (!Seguranca.master(ator)
                            || c.getEmpresaId() != 0 && c.getSolicitanteId() != 0) {
                        r.sendError(403);
                        return;
                    }
                    Usuario solicitante =
                            new UsuarioDAO().buscarPorId(inteiro(p.getParameter("solicitanteId")));
                    validarSolicitante(solicitante);
                    c.setEmpresaId(solicitante.getEmpresaId());
                    c.setSolicitanteId(solicitante.getId());
                    evento =
                            "Vínculos do chamado antigo regularizados. Data original não foi"
                                    + " presumida.";
                } else throw new IllegalArgumentException("Ação inválida.");
            }
            dao.salvar(c, ator, anexos, evento, anterior, interno);
            p.getSession().setAttribute("sucesso", "Chamado atualizado.");
            voltar(p, r, "/chamado-detalhes?id=" + c.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            p.getSession()
                    .setAttribute(
                            "erro",
                            e.getMessage() == null
                                    ? "Não foi possível processar o envio."
                                    : e.getMessage());
            voltar(
                    p,
                    r,
                    c != null && c.getId() > 0
                            ? "/chamado-detalhes?id=" + c.getId()
                            : "/chamados?acao=novo");
        } catch (ServletException e) {
            p.getSession()
                    .setAttribute(
                            "erro",
                            "Envie até 5 arquivos, com no máximo 10 MB cada e 20 MB no total.");
            voltar(p, r, "/chamados?acao=novo");
        }
    }

    private boolean podeEditar(Usuario ator, Chamado c) {
        return Seguranca.podeVer(ator, c)
                && "Aberto".equals(c.getStatus())
                && (Seguranca.administrador(ator) || c.getSolicitanteId() == ator.getId());
    }

    private void validarSolicitante(Usuario u) {
        Empresa empresa = u == null ? null : new EmpresaDAO().buscarPorId(u.getEmpresaId());
        if (u == null || !u.isAtivo() || empresa == null || !empresa.isAtivo())
            throw new IllegalArgumentException(
                    "Selecione um solicitante ativo vinculado a uma empresa ativa.");
    }

    private void formulario(HttpServletRequest p, Chamado c) {
        p.setAttribute("item", c);
        List<Categoria> categorias = new CategoriaDAO().listar();
        categorias.removeIf(k -> !k.isAtivo());
        p.setAttribute("categorias", categorias);
        if (Seguranca.master(Seguranca.usuario(p)))
            p.setAttribute("usuarios", new UsuarioDAO().listar());
    }

    private void detalhes(HttpServletRequest p, Chamado c) {
        p.setAttribute(
                "podeReabrir",
                Seguranca.podeReabrir(
                        Seguranca.usuario(p), c, new Timestamp(System.currentTimeMillis())));
        p.setAttribute("item", c);
        p.setAttribute("anexos", new AnexoDAO().listar(c.getId()));
        p.setAttribute(
                "historico",
                new HistoricoDAO().listar(c.getId(), Seguranca.equipe(Seguranca.usuario(p))));
        List<Usuario> equipe = new UsuarioDAO().listarEmpresa(c.getEmpresaId());
        equipe.removeIf(u -> !u.isAtivo() || !"Atendente".equals(u.getPerfil()));
        p.setAttribute("atendentes", equipe);
        if (Seguranca.master(Seguranca.usuario(p)))
            p.setAttribute("usuarios", new UsuarioDAO().listar());
    }

    private void listar(HttpServletRequest p) {
        List<Chamado> lista = new ArrayList<>(dao.listar(Seguranca.usuario(p)));
        String busca = Validacao.texto(p, "busca", 160, false).toLowerCase(java.util.Locale.ROOT);
        String status = Validacao.texto(p, "status", 30, false),
                prioridade = Validacao.texto(p, "prioridade", 20, false);
        int empresa = inteiro(p.getParameter("empresaId"));
        int categoria = inteiro(p.getParameter("categoriaId"));
        lista.removeIf(
                c ->
                        !busca.isEmpty()
                                        && !(c.getProtocolo()
                                                        + " "
                                                        + c.getTitulo()
                                                        + " "
                                                        + c.getDescricao()
                                                        + " "
                                                        + c.getSolicitanteNome())
                                                .toLowerCase(java.util.Locale.ROOT)
                                                .contains(busca)
                                || !status.isEmpty() && !status.equals(c.getStatus())
                                || !prioridade.isEmpty() && !prioridade.equals(c.getPrioridade())
                                || empresa > 0 && empresa != c.getEmpresaId()
                                || categoria > 0 && categoria != c.getCategoriaId());
        int paginas = Math.max(1, (lista.size() + 9) / 10),
                pagina = Math.max(1, Math.min(paginas, inteiro(p.getParameter("pagina"))));
        p.setAttribute("categorias", new CategoriaDAO().listar());
        p.setAttribute("total", lista.size());
        p.setAttribute("paginas", paginas);
        p.setAttribute("pagina", pagina);
        p.setAttribute(
                "itens", lista.subList((pagina - 1) * 10, Math.min(lista.size(), pagina * 10)));
        if (Seguranca.master(Seguranca.usuario(p)))
            p.setAttribute("empresas", new EmpresaDAO().listar());
    }

    private List<Anexo> lerAnexos(HttpServletRequest p) throws IOException, ServletException {
        List<Anexo> anexos = new ArrayList<>();
        long total = 0;
        if (p.getContentType() == null
                || !p.getContentType().toLowerCase().startsWith("multipart/")) return anexos;
        for (Part parte : p.getParts()) {
            if (!"anexos".equals(parte.getName())) continue;
            total += parte.getSize();
            if (parte.getSize() > 10485760 || total > 20971520 || anexos.size() >= 5)
                throw new IllegalArgumentException(
                        "Limite: 5 anexos, 10 MB cada e 20 MB no total.");
            String disposicao = parte.getHeader("content-disposition");
            java.util.regex.Matcher nome =
                    java.util.regex.Pattern.compile("filename=\"([^\"]*)\"")
                            .matcher(disposicao == null ? "" : disposicao);
            String arquivo = nome.find() ? nome.group(1) : "";
            if (parte.getSize() == 0) {
                if (arquivo.isEmpty()) continue;
                throw new IllegalArgumentException("Arquivo vazio. Selecione um anexo válido.");
            }
            arquivo = arquivo.replace('\\', '/');
            arquivo =
                    arquivo.substring(arquivo.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "");
            if (arquivo.isBlank() || arquivo.length() > 200)
                throw new IllegalArgumentException("Nome de arquivo inválido.");
            byte[] bytes;
            try (java.io.InputStream entrada = parte.getInputStream()) {
                bytes = entrada.readAllBytes();
            }
            String tipo;
            boolean pdf =
                    bytes.length > 8
                            && new String(bytes, 0, 5, java.nio.charset.StandardCharsets.US_ASCII)
                                    .equals("%PDF-");
            if (pdf && arquivo.toLowerCase().endsWith(".pdf")) {
                String fim =
                        new String(
                                bytes,
                                Math.max(0, bytes.length - 1024),
                                Math.min(1024, bytes.length),
                                java.nio.charset.StandardCharsets.US_ASCII);
                if (!fim.contains("%%EOF")) throw new IllegalArgumentException("PDF inválido.");
                tipo = "application/pdf";
            } else {
                boolean png =
                        bytes.length > 8
                                && bytes[0] == (byte) 137
                                && bytes[1] == 80
                                && bytes[2] == 78
                                && bytes[3] == 71;
                boolean jpg =
                        bytes.length > 3
                                && bytes[0] == (byte) 255
                                && bytes[1] == (byte) 216
                                && bytes[2] == (byte) 255;
                String minusculo = arquivo.toLowerCase(java.util.Locale.ROOT);
                if (!(png && minusculo.endsWith(".png")
                        || jpg && (minusculo.endsWith(".jpg") || minusculo.endsWith(".jpeg"))))
                    throw new IllegalArgumentException("Envie somente PNG, JPG ou PDF válidos.");
                if (!imagemValida(bytes))
                    throw new IllegalArgumentException(
                            "Imagem inválida ou com resolução acima de 20 milhões de pixels.");
                tipo = png ? "image/png" : "image/jpeg";
            }
            Anexo a = new Anexo();
            a.setNome(arquivo);
            a.setTipo(tipo);
            a.setConteudo(bytes);
            anexos.add(a);
        }
        return anexos;
    }

    private boolean imagemValida(byte[] bytes) throws IOException {
        try (javax.imageio.stream.ImageInputStream entrada =
                javax.imageio.ImageIO.createImageInputStream(
                        new java.io.ByteArrayInputStream(bytes))) {
            Iterator<javax.imageio.ImageReader> leitores =
                    javax.imageio.ImageIO.getImageReaders(entrada);
            if (!leitores.hasNext()) return false;
            javax.imageio.ImageReader leitor = leitores.next();
            try {
                leitor.setInput(entrada);
                long pixels = (long) leitor.getWidth(0) * leitor.getHeight(0);
                return pixels > 0 && pixels <= 20000000 && leitor.read(0) != null;
            } finally {
                leitor.dispose();
            }
        } catch (IOException e) {
            return false;
        }
    }
}
