<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<div class="login-grid cadastro-publico">
    <section class="apresentacao">
        <p class="rotulo">SOLICITAÇÃO DE ACESSO</p><h1>Seu acesso<br>começa aqui.</h1>
        <p>Informe seus dados e a empresa para a qual você trabalha. Um administrador analisará a solicitação.</p>
        <div class="passos"><p>01 · Envie a solicitação</p><p>02 · Aguarde a aprovação</p><p>03 · Entre no portal</p></div>
    </section>
    <section class="card login">
        <p class="rotulo">NOVO CADASTRO</p><h2>Solicitar acesso</h2><p>O perfil de administrador não pode ser solicitado por esta tela.</p>
        <form action="<%= ctx %>/confirmar" method="get">
            <label for="nome">Nome</label><input id="nome" required maxlength="150">
            <label for="email">E-mail</label><input id="email" type="email" required maxlength="200">
            <label for="telefone">Telefone</label><input id="telefone" type="tel" maxlength="30">
            <label for="empresa">Empresa</label><select id="empresa" required><option>Selecione sua empresa</option><option>Empresa de exemplo</option></select>
            <label for="perfil">Perfil solicitado</label><select id="perfil"><option>Colaborador</option><option>Atendente</option></select>
            <label for="senha-cadastro">Senha</label><div class="campo-senha"><input id="senha-cadastro" type="password" required><button type="button" class="secundario" data-mostrar-senha="senha-cadastro">Mostrar</button></div>
            <button class="largo" type="submit">Enviar solicitação</button>
        </form>
        <p class="ajuda">Já possui acesso? <a href="<%= ctx %>/login">Entrar no portal</a>.</p>
    </section>
</div>
<%@ include file="rodape.jspf" %>
