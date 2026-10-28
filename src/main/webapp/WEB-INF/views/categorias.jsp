<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<% Categoria item = (Categoria) request.getAttribute("item"); List<Categoria> itens = (List<Categoria>) request.getAttribute("itens"); %>
<%@ include file="cadastros.jspf" %>
<div class="cadastro-grid">
    <section class="card"><h2><%= item.getId() == 0 ? "Cadastrar categoria" : "Editar categoria" %></h2><p>Organize os tipos de atendimento da plataforma.</p>
        <form method="post" action="<%= ctx %>/categorias"><input type="hidden" name="id" value="<%= item.getId() %>">
            <label for="nome">Nome *</label><input id="nome" name="nome" value="<%= Web.html(item.getNome()) %>" required maxlength="160">
            <label for="descricao">Descrição</label><textarea id="descricao" name="descricao" rows="4" maxlength="500"><%= Web.html(item.getDescricao()) %></textarea>
            <label class="checkbox"><input type="checkbox" name="ativo" <%= item.isAtivo() ? "checked" : "" %>>Cadastro ativo</label><div class="acoes"><a class="botao secundario" href="<%= ctx %>/categorias">Cancelar</a><button type="submit">Salvar categoria</button></div>
        </form>
    </section>
    <section class="card"><h2>Categorias cadastradas</h2><div class="tabela"><table><thead><tr><th>Nome</th><th>Descrição</th><th>Situação</th><th>Ações</th></tr></thead><tbody>
        <% for (Categoria categoria : itens) { %><tr><td><strong><%= Web.html(categoria.getNome()) %></strong></td><td><%= Web.html(categoria.getDescricao()) %></td><td><span class="badge"><%= categoria.isAtivo() ? "Ativo" : "Inativo" %></span></td><td class="acoes"><a href="<%= ctx %>/categorias?id=<%= categoria.getId() %>">Editar</a><a class="perigo-texto" href="<%= ctx %>/categorias?acao=excluir&id=<%= categoria.getId() %>">Excluir</a></td></tr><% } %>
        <% if (itens.isEmpty()) { %><tr><td class="vazio" colspan="4">Nenhuma categoria cadastrada.</td></tr><% } %>
    </tbody></table></div></section>
</div>
<%@ include file="rodape.jspf" %>
