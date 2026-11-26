<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    Chamado item = (Chamado) request.getAttribute("item");
    boolean aberto = "Aberto".equals(item.getStatus());
    boolean ativo = !item.isEncerrado();
%>
<div class="titulo-acoes">
    <div>
        <p class="rotulo"><%= Web.html(item.getProtocolo()) %></p>
        <h1><%= Web.html(item.getTitulo()) %></h1>
        <span class="badge"><%= Web.html(item.getStatus()) %></span>
    </div>
    <div class="acoes">
        <button class="secundario" data-imprimir>Imprimir</button>
        <a href="<%= ctx %>/chamados">Voltar ao painel</a>
    </div>
</div>
<div class="detalhes-grid">
    <div>
        <section class="card">
            <h2>Detalhes do chamado</h2>
            <dl class="dados">
                <div>
                    <dt>Solicitante</dt>
                    <dd><%= Web.html(item.getSolicitanteNome()) %></dd>
                </div>
                <div>
                    <dt>Empresa</dt>
                    <dd><%= Web.html(item.getEmpresaNome()) %></dd>
                </div>
                <div>
                    <dt>Categoria</dt>
                    <dd><%= Web.html(item.getCategoria()) %></dd>
                </div>
                <div>
                    <dt>Prioridade</dt>
                    <dd><%= Web.html(item.getPrioridade()) %></dd>
                </div>
                <div>
                    <dt>Setor</dt>
                    <dd><%= Web.html(item.getSetor()) %></dd>
                </div>
                <div>
                    <dt>Abertura</dt>
                    <dd><%= Web.data(item.getCriadoEm()) %></dd>
                </div>
                <div>
                    <dt>Atendente</dt>
                    <dd><%= item.getResponsavelNome()==null?"Não atribuído":Web.html(item.getResponsavelNome()) %></dd>
                </div>
                <div>
                    <dt>Última atualização</dt>
                    <dd><%= Web.data(item.getAtualizadoEm()) %></dd>
                </div>
                <div>
                    <dt>Prazo de atendimento (SLA)</dt>
                    <dd><%= Web.data(item.getPrazoSla()) %><%= item.isAtrasado()?" · Atrasado":"" %></dd>
                </div>
                <div>
                    <dt>Resolução / encerramento</dt>
                    <dd><%= Web.data(item.getResolvidoEm()) %></dd>
                </div>
            </dl>
            <h3>Descrição</h3>
            <p class="preservar"><%= Web.html(item.getDescricao()) %></p>
            <%
                if (item.getSolucao() != null) {
            %>
                <h3>Solução</h3>
                <p class="preservar"><%= Web.html(item.getSolucao()) %></p>
            <%
                }
            %>
        </section>
        <section class="card">
            <h2>Histórico e comentários</h2>
            <ol class="historico">
                <%
                    for (Historico h : (List<Historico>) request.getAttribute("historico")) {
                %>
                    <li>
                        <div>
                            <strong><%= Web.html(h.getAutor()) %></strong>
                            <%
                                if (h.isInterno()) {
                            %>
                                <span class="badge">Nota interna</span>
                            <%
                                }
                            %>
                            <small><%= Web.data(h.getCriadoEm()) %></small>
                        </div>
                        <p class="preservar"><%= Web.html(h.getTexto()) %></p>
                    </li>
                <%
                    }
                %>
            </ol>
            <form method="post" action="<%= ctx %>/chamados">
                <%= util.Seguranca.campoCsrf(request) %>
                <input type="hidden" name="id" value="<%= item.getId() %>">
                <input type="hidden" name="acao" value="comentar">
                <label>Comentário</label>
                <textarea name="texto" maxlength="5000" required></textarea>
                <%
                    if (util.Seguranca.equipe(logado)) {
                %>
                    <label class="checkbox"><input type="checkbox" name="interno">Nota interna (somente equipe de atendimento)</label>
                <%
                    }
                %>
                <button>Enviar comentário</button>
            </form>
        </section>
        <section class="card">
            <h2>Anexos</h2>
            <ul class="lista">
                <%
                    List<Anexo> anexos = (List<Anexo>) request.getAttribute("anexos");
                    for (Anexo a : anexos) {
                %>
                    <li>
                        <a href="<%= ctx %>/anexos?id=<%= a.getId() %>"><%= Web.html(a.getNome()) %></a>
                        <small>Enviado por <%= a.getAutorNome()==null?"Não informado":Web.html(a.getAutorNome()) %> · <%= Web.data(a.getCriadoEm()) %></small>
                        <%
                            if (util.Seguranca.administrador(logado) || a.getAutorId() == logado.getId()) {
                        %>
                            <form method="post" action="<%= ctx %>/anexos">
                                <%= util.Seguranca.campoCsrf(request) %>
                                <input type="hidden" name="id" value="<%= a.getId() %>">
                                <label class="checkbox"><input type="checkbox" required>Confirmar exclusão deste anexo</label>
                                <button class="secundario" name="acao" value="excluir">Excluir anexo</button>
                            </form>
                        <%
                            }
                        %>
                    </li>
                <%
                    }
                    if (anexos.isEmpty()) {
                %>
                    <li>Nenhum anexo enviado.</li>
                <%
                    }
                %>
            </ul>
        </section>
    </div>
    <aside class="painel-acoes">
        <%
            if (item.getEmpresaId() == 0 || item.getSolicitanteId() == 0) {
        %>
            <section class="card">
                <h2>Regularizar chamado antigo</h2>
                <p>Este registro da etapa de CRUD ainda não tem vínculos. Nenhuma empresa foi presumida.</p>
                <form method="post" action="<%= ctx %>/chamados">
                    <%= util.Seguranca.campoCsrf(request) %>
                    <input type="hidden" name="id" value="<%= item.getId() %>">
                    <input type="hidden" name="acao" value="regularizar">
                    <label>Solicitante / empresa</label>
                    <select name="solicitanteId" required>
                        <option value="">Selecione</option>
                        <%
                            for (Usuario u : (List<Usuario>) request.getAttribute("usuarios")) {
                                if (u.getEmpresaId() > 0 && u.isAtivo()) {
                        %>
                                <option value="<%= u.getId() %>"><%= Web.html(u.getNome()) %> — <%= Web.html(u.getEmpresaNome()) %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <button>Vincular</button>
                </form>
            </section>
        <%
            }
        %>
        <%
            if (ativo && util.Seguranca.equipe(logado)) {
        %>
            <section class="card">
                <h2>Gerenciar atendimento</h2>
                <form method="post" action="<%= ctx %>/chamados">
                    <%= util.Seguranca.campoCsrf(request) %>
                    <input type="hidden" name="id" value="<%= item.getId() %>">
                    <input type="hidden" name="acao" value="status">
                    <label>Novo status</label>
                    <select name="status">
                        <%
                            for (String status :
                                    Arrays.asList(
                                            "Aberto",
                                            "Em atendimento",
                                            "Aguardando contato",
                                            "Aguardando aprovação",
                                            "Recusado")) {
                        %>
                            <option <%= Web.selecionado(item.getStatus(),status) %>><%= status %></option>
                        <%
                            }
                        %>
                    </select>
                    <label>Motivo (obrigatório para recusar)</label>
                    <textarea name="motivo" maxlength="4800"></textarea>
                    <button>Atualizar status</button>
                </form>
                <%
                    if (aberto && item.getResponsavelId() == 0) {
                %>
                    <form method="post" action="<%= ctx %>/chamados">
                        <%= util.Seguranca.campoCsrf(request) %>
                        <input type="hidden" name="id" value="<%= item.getId() %>">
                        <button name="acao" value="assumir">Assumir atendimento</button>
                    </form>
                <%
                    }
                %>
                <%
                    if (util.Seguranca.administrador(logado)) {
                %>
                    <form method="post" action="<%= ctx %>/chamados">
                        <%= util.Seguranca.campoCsrf(request) %>
                        <input type="hidden" name="id" value="<%= item.getId() %>">
                        <input type="hidden" name="acao" value="atribuir">
                        <label>Atendente</label>
                        <select name="responsavelId" required>
                            <option value="">Selecione</option>
                            <%
                                for (Usuario u : (List<Usuario>) request.getAttribute("atendentes")) {
                            %>
                                <option value="<%= u.getId() %>" <%= Web.selecionado(item.getResponsavelId(),u.getId()) %>><%= Web.html(u.getNome()) %></option>
                            <%
                                }
                            %>
                        </select>
                        <button>Atribuir atendimento</button>
                    </form>
                <%
                    }
                %>
                <%
                    if ("Em atendimento".equals(item.getStatus())
                            && (util.Seguranca.administrador(logado)
                                    || item.getResponsavelId() == logado.getId())) {
                %>
                    <form method="post" action="<%= ctx %>/chamados">
                        <%= util.Seguranca.campoCsrf(request) %>
                        <input type="hidden" name="id" value="<%= item.getId() %>">
                        <input type="hidden" name="acao" value="resolver">
                        <label>Solução aplicada</label>
                        <textarea name="solucao" maxlength="5000" required></textarea>
                        <button>Resolver chamado</button>
                    </form>
                <%
                    }
                %>
            </section>
        <%
            }
        %>
        <%
            if (ativo
                    && (util.Seguranca.equipe(logado)
                            || aberto && item.getSolicitanteId() == logado.getId())) {
        %>
            <section class="card">
                <h2>Cancelar chamado</h2>
                <form method="post" action="<%= ctx %>/chamados">
                    <%= util.Seguranca.campoCsrf(request) %>
                    <input type="hidden" name="id" value="<%= item.getId() %>">
                    <input type="hidden" name="acao" value="cancelar">
                    <label>Motivo</label>
                    <textarea name="motivo" maxlength="4800" required></textarea>
                    <button class="secundario">Confirmar cancelamento</button>
                </form>
            </section>
        <%
            }
        %>
        <%
            if (Boolean.TRUE.equals(request.getAttribute("podeReabrir"))) {
        %>
            <section class="card">
                <h2>Reabrir chamado</h2>
                <p>O solicitante pode reabrir resolvidos em até sete dias. O prazo original de SLA é mantido.</p>
                <form method="post" action="<%= ctx %>/chamados">
                    <%= util.Seguranca.campoCsrf(request) %>
                    <input type="hidden" name="id" value="<%= item.getId() %>">
                    <button class="secundario" name="acao" value="reabrir">Reabrir chamado</button>
                </form>
            </section>
        <%
            }
        %>
        <%
            if ("Resolvido".equals(item.getStatus())) {
        %>
            <section class="card">
                <h2>Avaliação do atendimento</h2>
                <%
                    if (item.getAvaliacao() > 0) {
                %>
                    <p>Nota enviada: <%= item.getAvaliacao() %> / 5</p>
                    <p class="preservar"><%= Web.html(item.getComentarioAvaliacao()) %></p>
                <%
                    }
                %>
                <%
                    if (item.getSolicitanteId() == logado.getId()) {
                %>
                    <form method="post" action="<%= ctx %>/chamados">
                        <%= util.Seguranca.campoCsrf(request) %>
                        <input type="hidden" name="id" value="<%= item.getId() %>">
                        <input type="hidden" name="acao" value="avaliar">
                        <label>Nota de 1 a 5</label>
                        <select name="avaliacao">
                            <%
                                for (int n = 1; n <= 5; n++) {
                            %>
                                <option value="<%= n %>" <%= Web.selecionado(item.getAvaliacao(),n) %>><%= n %></option>
                            <%
                                }
                            %>
                        </select>
                        <label>Comentário (opcional)</label>
                        <textarea name="comentarioAvaliacao" maxlength="1000"><%= Web.html(item.getComentarioAvaliacao()) %></textarea>
                        <button><%= item.getAvaliacao()>0?"Atualizar avaliação":"Enviar avaliação" %></button>
                    </form>
                    <%
                        if (item.getAvaliacao() > 0) {
                    %>
                        <form method="post" action="<%= ctx %>/chamados">
                            <%= util.Seguranca.campoCsrf(request) %>
                            <input type="hidden" name="id" value="<%= item.getId() %>">
                            <button class="secundario" name="acao" value="remover-avaliacao">Remover avaliação</button>
                        </form>
                <%
                        }
                    }
                %>
            </section>
        <%
            }
        %>
        <%
            if (util.Seguranca.administrador(logado)
                    || aberto && item.getSolicitanteId() == logado.getId()) {
        %>
            <section class="card">
                <h2>Excluir registro</h2>
                <p>Esta operação remove o chamado, seus anexos e histórico. Prefira cancelar para manter o atendimento registrado.</p>
                <form method="post" action="<%= ctx %>/chamados">
                    <%= util.Seguranca.campoCsrf(request) %>
                    <input type="hidden" name="id" value="<%= item.getId() %>">
                    <label class="checkbox"><input type="checkbox" required>Confirmo a exclusão</label>
                    <button class="secundario" name="acao" value="excluir">Excluir chamado</button>
                </form>
            </section>
        <%
            }
        %>
    </aside>
</div>
<%@ include file="rodape.jspf" %>
