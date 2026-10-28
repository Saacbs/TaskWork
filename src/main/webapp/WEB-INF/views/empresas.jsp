<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<% Empresa item = (Empresa) request.getAttribute("item"); List<Empresa> itens = (List<Empresa>) request.getAttribute("itens"); %>
<%@ include file="cadastros.jspf" %>
<div class="cadastro-grid">
    <section class="card"><h2><%= item.getId() == 0 ? "Cadastrar empresa" : "Editar empresa" %></h2><p>Preencha os dados para registrar a empresa no sistema.</p>
        <form method="post" action="<%= ctx %>/empresas"><input type="hidden" name="id" value="<%= item.getId() %>">
            <label for="nome">Nome da empresa *</label><input id="nome" name="nome" value="<%= Web.html(item.getNome()) %>" required maxlength="160">
            <label for="cnpj">CNPJ *</label><input id="cnpj" name="cnpj" value="<%= Web.html(item.getCnpj()) %>" required maxlength="18" placeholder="00.000.000/0001-00">
            <label class="checkbox"><input type="checkbox" name="ativo" <%= item.isAtivo() ? "checked" : "" %>>Cadastro ativo</label>
            <div class="acoes"><a class="botao secundario" href="<%= ctx %>/empresas">Cancelar</a><button type="submit">Salvar empresa</button></div>
        </form>
    </section>
    <section class="card"><h2>Empresas cadastradas</h2><p>Consulte, edite ou exclua os registros existentes.</p><div class="tabela"><table><thead><tr><th>Nome</th><th>CNPJ</th><th>Situação</th><th>Ações</th></tr></thead><tbody>
        <% for (Empresa empresa : itens) { %><tr><td><strong><%= Web.html(empresa.getNome()) %></strong></td><td><%= Web.html(empresa.getCnpj()) %></td><td><span class="badge"><%= empresa.isAtivo() ? "Ativo" : "Inativo" %></span></td><td class="acoes"><a href="<%= ctx %>/empresas?id=<%= empresa.getId() %>">Editar</a><a class="perigo-texto" href="<%= ctx %>/empresas?acao=excluir&id=<%= empresa.getId() %>">Excluir</a></td></tr><% } %>
        <% if (itens.isEmpty()) { %><tr><td class="vazio" colspan="4">Nenhuma empresa cadastrada.</td></tr><% } %>
    </tbody></table></div></section>
</div>
<%@ include file="rodape.jspf" %>
