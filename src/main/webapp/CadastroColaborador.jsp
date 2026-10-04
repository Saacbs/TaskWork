<%@ page import="model.Empresa" %>
<%@ page import="java.util.List" %>

<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Colaborador</title>
</head>
<body>

<h2>Criar Colaborador</h2>

<form action="create-colaborador" method="post">

    <label>Digite o nome do colaborador:</label>
    <input type="text" name="nome" id="nome">

    <label>Digite o email do colaborador:</label>
    <input type="text" name="email" id="email">

    <label>Digite o telefone do colaborador:</label>
    <input type="text" name="telefone" id="telefone">

    <label>Empresa:</label>

    <select name="idDaEmpresa" id="idDaEmpresa">

        <%
        List<Empresa> empresas =
        (List<Empresa>) request.getAttribute("empresas");

            for (Empresa empresa : empresas) {
            %>

            <option value="<%= empresa.getId() %>">
                <%= empresa.getName() %>
            </option>

            <%
            }
            %>

    </select>

    <button type="submit">Cadastrar</button>

    <button type="button" onclick="window.location.href='index.html'">
        Voltar
    </button>

</form>

</body>
</html>