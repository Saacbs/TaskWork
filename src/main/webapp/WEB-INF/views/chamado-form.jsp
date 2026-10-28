<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<% Chamado item = (Chamado) request.getAttribute("item"); boolean editando = item.getId() > 0; %>
<a class="voltar-painel" href="<%= ctx %>/chamados">← Voltar ao painel</a>
<p class="rotulo"><%= editando ? "EDIÇÃO DA SOLICITAÇÃO" : "NOVA SOLICITAÇÃO" %></p><h1><%= editando ? "Editar chamado" : "Abrir novo chamado" %></h1><p class="subtitulo-formulario">Forneça detalhes claros para acelerar o atendimento.</p>
<form class="card formulario formulario-chamado" method="post" action="<%= ctx %>/chamados"><input type="hidden" name="id" value="<%= item.getId() %>">
    <section class="etapa-formulario"><div class="titulo-etapa"><span>1</span><div><h2>Sobre o problema</h2><small>Descreva o que está acontecendo</small></div></div>
        <div class="duas-colunas"><div><label for="categoria">Categoria *</label><input id="categoria" name="categoria" value="<%= Web.html(item.getCategoria()) %>" required maxlength="160" placeholder="Ex.: Acesso ao sistema"></div><div><label for="prioridade">Prioridade *</label><select id="prioridade" name="prioridade"><option value="Baixa" <%= Web.selecionado(item.getPrioridade(), "Baixa") %>>Baixa</option><option value="Média" <%= Web.selecionado(item.getPrioridade(), "Média") %>>Média</option><option value="Alta" <%= Web.selecionado(item.getPrioridade(), "Alta") %>>Alta</option><option value="Crítica" <%= Web.selecionado(item.getPrioridade(), "Crítica") %>>Crítica</option></select></div></div>
        <label for="setor">Setor em que você trabalha *</label><input id="setor" name="setor" maxlength="100" required value="<%= Web.html(item.getSetor()) %>" placeholder="Ex.: Financeiro, RH, Operações">
        <label for="titulo">Título *</label><input id="titulo" name="titulo" value="<%= Web.html(item.getTitulo()) %>" required maxlength="160" placeholder="Ex.: Não consigo acessar a VPN">
        <label for="descricao">Descrição detalhada *</label><textarea id="descricao" name="descricao" rows="5" required maxlength="5000" placeholder="Conte quando o problema começou e o que você já tentou."><%= Web.html(item.getDescricao()) %></textarea>
        <label for="status">Status *</label><select id="status" name="status"><option value="Aberto" <%= Web.selecionado(item.getStatus(), "Aberto") %>>Aberto</option><option value="Em atendimento" <%= Web.selecionado(item.getStatus(), "Em atendimento") %>>Em atendimento</option><option value="Resolvido" <%= Web.selecionado(item.getStatus(), "Resolvido") %>>Resolvido</option></select>
        <% if (!editando) { %><label class="caixa-anexo"><span>＋ Adicionar anexo</span><small>PNG, JPG ou PDF · máximo de 10 MB por arquivo</small><input type="file" accept="image/png,image/jpeg,application/pdf"></label><% } %>
    </section>
    <div class="acoes"><a class="botao secundario" href="<%= ctx %>/chamados">Cancelar</a><button type="submit"><%= editando ? "Salvar alterações" : "Enviar chamado →" %></button></div>
</form>
<%@ include file="rodape.jspf" %>
