<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<% List<Chamado> itens = (List<Chamado>) request.getAttribute("itens"); %>
<div class="titulo-acoes"><div><p class="rotulo">CENTRAL DE ATENDIMENTO</p><h1>Chamados</h1><p>Pesquise e acompanhe as solicitações cadastradas.</p></div><a class="botao" href="<%= ctx %>/chamados?acao=novo">+ Novo chamado</a></div>
<section class="card"><form class="filtros"><div><label for="pesquisa">Pesquisar</label><input id="pesquisa" placeholder="Título, categoria ou setor"></div><div><label for="status">Status</label><select id="status"><option>Todos</option><option>Aberto</option><option>Em atendimento</option><option>Resolvido</option></select></div><button type="button">Filtrar</button></form>
<div class="tabela"><table><thead><tr><th>Protocolo / título</th><th>Categoria</th><th>Prioridade</th><th>Status</th><th>Ações</th></tr></thead><tbody>
    <% for (Chamado chamado : itens) { %><tr><td><strong>#<%= chamado.getId() %></strong><small><%= Web.html(chamado.getTitulo()) %></small></td><td><%= Web.html(chamado.getCategoria()) %><small><%= Web.html(chamado.getSetor()) %></small></td><td><%= Web.html(chamado.getPrioridade()) %></td><td><span class="badge"><%= Web.html(chamado.getStatus()) %></span></td><td class="acoes"><a href="<%= ctx %>/chamado-detalhes">Detalhes</a><a href="<%= ctx %>/chamados?acao=editar&id=<%= chamado.getId() %>">Editar</a><a class="perigo-texto" href="<%= ctx %>/chamados?acao=excluir&id=<%= chamado.getId() %>">Excluir</a></td></tr><% } %>
    <% if (itens.isEmpty()) { %><tr><td class="vazio" colspan="5">Nenhum chamado cadastrado.</td></tr><% } %>
</tbody></table></div></section>
<%@ include file="rodape.jspf" %>
