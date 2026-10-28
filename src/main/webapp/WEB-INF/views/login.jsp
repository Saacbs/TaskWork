<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<div class="login-grid">
    <section class="apresentacao">
        <p class="rotulo">CENTRAL DE ATENDIMENTO</p>
        <h1>Seu suporte,<br>mais próximo.</h1>
        <p>Abra solicitações e acompanhe cada etapa do atendimento em um só lugar.</p>
        <div class="passos"><p>01 · Registre sua solicitação</p><p>02 · Acompanhe o atendimento</p><p>03 · Avalie a solução</p></div>
    </section>
    <section class="card login">
        <p class="rotulo">BEM-VINDO AO TASKWORK</p><h2>Acesse sua conta</h2><p>Informe seus dados para continuar.</p>
        <form action="<%= ctx %>/painel" method="get">
            <label for="email">E-mail</label><input id="email" type="email" placeholder="nome@empresa.com" required>
            <label for="senha">Senha</label><div class="campo-senha"><input id="senha" type="password" required><button type="button" class="secundario" data-mostrar-senha="senha">Mostrar</button></div>
            <button class="largo" type="submit">Entrar</button>
        </form>
        <p class="ajuda">Ainda não possui acesso? <a href="<%= ctx %>/cadastro">Solicitar cadastro</a>.</p>
    </section>
</div>
<%@ include file="rodape.jspf" %>
