<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    Chamado item = (Chamado) request.getAttribute("item");
    boolean editando = item.getId() > 0;
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
%>
<a class="voltar-painel" href="<%= ctx %>/chamados">← Voltar ao painel</a>
<p class="rotulo"><%= editando?"EDIÇÃO DA SOLICITAÇÃO":"NOVA SOLICITAÇÃO" %></p>
<h1><%= editando?"Editar chamado":"Abrir novo chamado" %></h1>
<p class="subtitulo-formulario">Forneça detalhes claros para acelerar o atendimento.</p>
<form class="card formulario formulario-chamado" method="post" enctype="multipart/form-data" action="<%= ctx %>/chamados?csrf=<%= util.Seguranca.csrf(request) %>">
    <input type="hidden" name="id" value="<%= item.getId() %>">
    <input type="hidden" name="acao" value="salvar">
    <section class="etapa-formulario">
        <div class="titulo-etapa">
            <span>1</span>
            <div>
                <h2>Sobre o problema</h2>
                <small>Descreva o que está acontecendo</small>
            </div>
        </div>
        <%
            if (util.Seguranca.master(logado) && !editando) {
        %>
            <label>Solicitante / empresa *</label>
            <select name="solicitanteId" required>
                <option value="">Selecione</option>
                <%
                    for (Usuario u : (List<Usuario>) request.getAttribute("usuarios")) {
                        if (u.isAtivo() && u.getEmpresaId() > 0) {
                %>
                        <option value="<%= u.getId() %>"><%= Web.html(u.getNome()) %> — <%= Web.html(u.getEmpresaNome()) %></option>
                <%
                        }
                    }
                %>
            </select>
        <%
            }
        %>
        <div class="duas-colunas">
            <div>
                <label for="categoriaId">Categoria *</label>
                <select id="categoriaId" name="categoriaId" required>
                    <option value="">Selecione</option>
                    <%
                        for (Categoria categoria : categorias) {
                    %>
                        <option value="<%= categoria.getId() %>" <%= Web.selecionado(item.getCategoriaId(),categoria.getId()) %>><%= Web.html(categoria.getNome()) %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div>
                <label for="prioridade">Prioridade *</label>
                <select id="prioridade" name="prioridade">
                    <%
                        for (String prioridade : Arrays.asList("Baixa", "Média", "Alta", "Crítica")) {
                    %>
                        <option <%= Web.selecionado(item.getPrioridade(),prioridade) %>><%= prioridade %></option>
                    <%
                        }
                    %>
                </select>
            </div>
        </div>
        <label for="setor">Setor em que você trabalha *</label>
        <input id="setor" name="setor" maxlength="100" required value="<%= Web.html(item.getSetor()) %>" placeholder="Ex.: Financeiro, RH, Operações">
        <label for="titulo">Título *</label>
        <input id="titulo" name="titulo" value="<%= Web.html(item.getTitulo()) %>" required maxlength="160" placeholder="Ex.: Não consigo acessar a VPN">
        <label for="descricao">Descrição detalhada *</label>
        <textarea id="descricao" name="descricao" rows="5" required maxlength="5000"><%= Web.html(item.getDescricao()) %></textarea>
        <p>Status: <strong><%= Web.html(item.getStatus()) %></strong></p>
        <label class="caixa-anexo"><span>＋ Adicionar anexos</span><small>PNG, JPG ou PDF · até 5 arquivos, 10 MB por arquivo e 20 MB no total</small><input type="file" name="anexos" multiple accept="image/png,image/jpeg,application/pdf"></label>
    </section>
    <div class="acoes">
        <a class="botao secundario" href="<%= ctx %>/chamados">Cancelar</a>
        <button><%= editando?"Salvar alterações":"Enviar chamado →" %></button>
    </div>
</form>
<%@ include file="rodape.jspf" %>
