<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<section class="card login">
    <h1>Configuração inicial</h1>
    <p>Crie o administrador master usando a chave inicial exibida no terminal do servidor.</p>
    <form method="post" action="<%= ctx %>/configuracao">
        <%= util.Seguranca.campoCsrf(request) %>
        <label>Chave inicial</label>
        <input name="chave" required autocomplete="off">
        <label>Nome</label>
        <input name="nome" maxlength="160" required>
        <label>E-mail</label>
        <input name="email" type="email" maxlength="160" required>
        <label>Senha (8 a 100 caracteres)</label>
        <input name="senha" type="password" minlength="8" maxlength="100" required>
        <button>Criar administrador master</button>
    </form>
</section>
<%@ include file="rodape.jspf" %>
