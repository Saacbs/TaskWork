<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<% Usuario item = (Usuario) request.getAttribute("item"); List<Usuario> itens = (List<Usuario>) request.getAttribute("itens"); List<Empresa> empresas = (List<Empresa>) request.getAttribute("empresas"); %>
<%@ include file="cadastros.jspf" %>
<div class="cadastro-grid">
    <section class="card"><h2><%= item.getId() == 0 ? "Cadastrar usuário" : "Editar usuário" %></h2><p>Cadastre Administrador, Colaborador ou Atendente.</p>
        <form method="post" action="<%= ctx %>/usuarios"><input type="hidden" name="id" value="<%= item.getId() %>">
            <label for="nome">Nome *</label><input id="nome" name="nome" value="<%= Web.html(item.getNome()) %>" required maxlength="160">
            <label for="email">E-mail *</label><input id="email" name="email" type="email" value="<%= Web.html(item.getEmail()) %>" required maxlength="160">
            <label for="telefone">Telefone</label><input id="telefone" name="telefone" value="<%= Web.html(item.getTelefone()) %>" maxlength="25">
            <label for="empresaId">Empresa *</label><select id="empresaId" name="empresaId" required><option value="">Selecione</option><% for (Empresa empresa : empresas) { %><option value="<%= empresa.getId() %>" <%= Web.selecionado(item.getEmpresaId(), empresa.getId()) %>><%= Web.html(empresa.getNome()) %></option><% } %></select>
            <label for="perfil">Perfil *</label><select id="perfil" name="perfil"><option value="Administrador" <%= Web.selecionado(item.getPerfil(), "Administrador") %>>Administrador</option><option value="Colaborador" <%= Web.selecionado(item.getPerfil(), "Colaborador") %>>Colaborador</option><option value="Atendente" <%= Web.selecionado(item.getPerfil(), "Atendente") %>>Atendente</option></select>
            <label class="checkbox"><input type="checkbox" name="ativo" <%= item.isAtivo() ? "checked" : "" %>>Cadastro ativo</label><div class="acoes"><a class="botao secundario" href="<%= ctx %>/usuarios">Cancelar</a><button type="submit">Salvar usuário</button></div>
        </form>
    </section>
    <section class="card"><h2>Usuários cadastrados</h2><div class="tabela"><table><thead><tr><th>Nome</th><th>E-mail / Empresa</th><th>Perfil</th><th>Situação</th><th>Ações</th></tr></thead><tbody>
        <% for (Usuario usuario : itens) { %><tr><td><strong><%= Web.html(usuario.getNome()) %></strong></td><td><%= Web.html(usuario.getEmail()) %><small><%= Web.html(usuario.getEmpresaNome()) %></small></td><td><%= Web.html(usuario.getPerfil()) %></td><td><span class="badge"><%= usuario.isAtivo() ? "Ativo" : "Inativo" %></span></td><td class="acoes"><a href="<%= ctx %>/usuarios?id=<%= usuario.getId() %>">Editar</a><a class="perigo-texto" href="<%= ctx %>/usuarios?acao=excluir&id=<%= usuario.getId() %>">Excluir</a></td></tr><% } %>
        <% if (itens.isEmpty()) { %><tr><td class="vazio" colspan="5">Nenhum usuário cadastrado.</td></tr><% } %>
    </tbody></table></div></section>
</div>
<%@ include file="rodape.jspf" %>
