<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<div class="login-grid cadastro-publico">
    <section class="apresentacao">
        <p class="rotulo">SOLICITAÇÃO DE ACESSO</p>
        <h1>Seu acesso<br>começa aqui.</h1>
        <p>Informe seus dados e a empresa para a qual você trabalha. Um administrador analisará a solicitação.</p>
        <div class="passos">
            <p>01 · Envie a solicitação</p>
            <p>02 · Aguarde a aprovação</p>
            <p>03 · Entre no portal</p>
        </div>
    </section>
    <section class="card login">
        <p class="rotulo">NOVO CADASTRO</p>
        <h2>Solicitar acesso</h2>
        <p>O perfil de administrador não pode ser solicitado por esta tela.</p>
        <form action="<%= ctx %>/cadastro" method="post">
            <%= util.Seguranca.campoCsrf(request) %>
            <label for="nome">Nome</label>
            <input id="nome" name="nome" required maxlength="160" value="<%= Web.html(request.getParameter("nome")) %>">
            <label for="email">E-mail</label>
            <input id="email" name="email" type="email" required maxlength="160" value="<%= Web.html(request.getParameter("email")) %>">
            <label for="telefone">Telefone</label>
            <input id="telefone" name="telefone" type="tel" maxlength="25" value="<%= Web.html(request.getParameter("telefone")) %>">
            <label for="empresaId">Empresa</label>
            <select id="empresaId" name="empresaId" required>
                <option value="">Selecione sua empresa</option>
                <%
                    for (Empresa empresa : (List<Empresa>) request.getAttribute("empresas")) {
                %>
                    <option value="<%= empresa.getId() %>" <%= Web.selecionado(request.getParameter("empresaId"),empresa.getId()) %>><%= Web.html(empresa.getNome()) %></option>
                <%
                    }
                %>
            </select>
            <label for="perfil">Perfil solicitado</label>
            <select id="perfil" name="perfil">
                <option>Colaborador</option>
                <option>Atendente</option>
            </select>
            <label for="senha-cadastro">Senha (8 a 100 caracteres)</label>
            <div class="campo-senha">
                <input id="senha-cadastro" name="senha" type="password" minlength="8" maxlength="100" required>
                <button type="button" class="secundario" data-mostrar-senha="senha-cadastro">Mostrar</button>
            </div>
            <button class="largo">Enviar solicitação</button>
        </form>
        <p class="ajuda">Já possui acesso? <a href="<%= ctx %>/login">Entrar no portal</a>.</p>
    </section>
</div>
<%@ include file="rodape.jspf" %>
