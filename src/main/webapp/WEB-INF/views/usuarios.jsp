<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    Usuario item = (Usuario) request.getAttribute("item");
    List<Usuario> itens = (List<Usuario>) request.getAttribute("itens");
    List<Empresa> empresas = (List<Empresa>) request.getAttribute("empresas");
%>
<%@ include file="cadastros.jspf" %>
<div class="cadastro-grid">
    <section class="card">
        <h2><%= item.getId() == 0 ? "Cadastrar usuário" : "Editar usuário" %></h2>
        <p>Gerencie os usuários permitidos ao seu perfil.</p>
        <form method="post" action="<%= ctx %>/usuarios">
            <%= util.Seguranca.campoCsrf(request) %>
            <input type="hidden" name="id" value="<%= item.getId() %>">
            <label for="nome">Nome *</label>
            <input id="nome" name="nome" value="<%= Web.html(item.getNome()) %>" required maxlength="160">
            <label for="email">E-mail *</label>
            <input id="email" name="email" type="email" value="<%= Web.html(item.getEmail()) %>" required maxlength="160">
            <label for="telefone">Telefone</label>
            <input id="telefone" name="telefone" value="<%= Web.html(item.getTelefone()) %>" maxlength="25">
            <label for="empresaId">Empresa *</label>
            <select id="empresaId" name="empresaId" required>
                <option value="">Selecione</option>
                <%
                    for (Empresa empresa : empresas) {
                %>
                    <option value="<%= empresa.getId() %>" <%= Web.selecionado(item.getEmpresaId(), empresa.getId()) %>><%= Web.html(empresa.getNome()) %></option>
                <%
                    }
                %>
            </select>
            <label for="perfil">Perfil *</label>
            <select id="perfil" name="perfil">
                <%
                    if (util.Seguranca.master(logado)) {
                %>
                    <option value="Administrador" <%= Web.selecionado(item.getPerfil(), "Administrador") %>>Administrador</option>
                <%
                    }
                %>
                <option value="Colaborador" <%= Web.selecionado(item.getPerfil(), "Colaborador") %>>Colaborador</option>
                <option value="Atendente" <%= Web.selecionado(item.getPerfil(), "Atendente") %>>Atendente</option>
            </select>
            <label for="senha">Senha (obrigatória no novo cadastro; deixe em branco para manter)</label>
            <input id="senha" name="senha" type="password" maxlength="100" autocomplete="new-password">
            <label class="checkbox"><input type="checkbox" name="ativo" <%= item.isAtivo() ? "checked" : "" %>>Cadastro ativo</label>
            <div class="acoes">
                <a class="botao secundario" href="<%= ctx %>/usuarios">Cancelar</a>
                <button type="submit">Salvar usuário</button>
            </div>
        </form>
    </section>
    <section class="card">
        <h2>Usuários cadastrados</h2>
        <div class="tabela">
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>E-mail / Empresa</th>
                        <th>Perfil</th>
                        <th>Situação</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Usuario usuario : itens) {
                    %>
                        <tr>
                            <td><strong><%= Web.html(usuario.getNome()) %></strong></td>
                            <td><%= Web.html(usuario.getEmail()) %><small><%= Web.html(usuario.getEmpresaNome()) %></small></td>
                            <td><%= Web.html(usuario.getPerfil()) %></td>
                            <td><span class="badge"><%= usuario.isAtivo() ? "Ativo" : "Inativo" %></span></td>
                            <td class="acoes">
                                <a href="<%= ctx %>/usuarios?id=<%= usuario.getId() %>">Editar</a>
                                <form method="post" action="<%= ctx %>/usuarios">
                                    <%= util.Seguranca.campoCsrf(request) %>
                                    <input type="hidden" name="id" value="<%= usuario.getId() %>">
                                    <button class="secundario" name="acao" value="excluir">Excluir</button>
                                </form>
                            </td>
                        </tr>
                    <%
                        }
                    %>
                    <%
                        if (itens.isEmpty()) {
                    %>
                        <tr>
                            <td class="vazio" colspan="5">Nenhum usuário cadastrado.</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </section>
</div>
<%@ include file="rodape.jspf" %>
