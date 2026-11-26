<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    List<Chamado> itens = (List<Chamado>) request.getAttribute("itens");
%>
<div class="titulo-acoes">
    <div>
        <p class="rotulo">CENTRAL DE ATENDIMENTO</p>
        <h1>Chamados</h1>
        <p>Pesquise e acompanhe as solicitações cadastradas.</p>
    </div>
    <a class="botao" href="<%= ctx %>/chamados?acao=novo">+ Novo chamado</a>
</div>
<section class="card">
    <form class="filtros" method="get" action="<%= ctx %>/chamados">
        <div>
            <label for="busca">Pesquisar</label>
            <input id="busca" name="busca" placeholder="Protocolo, título, descrição ou solicitante" maxlength="160" value="<%= Web.html(request.getParameter("busca")) %>">
        </div>
        <div>
            <label for="status">Status</label>
            <select id="status" name="status">
                <option value="">Todos</option>
                <%
                    for (String s : Chamado.STATUS) {
                %>
                    <option <%= Web.selecionado(request.getParameter("status"),s) %>><%= s %></option>
                <%
                    }
                %>
            </select>
        </div>
        <div>
            <label>Prioridade</label>
            <select name="prioridade">
                <option value="">Todas</option>
                <%
                    for (String s : Arrays.asList("Baixa", "Média", "Alta", "Crítica")) {
                %>
                    <option <%= Web.selecionado(request.getParameter("prioridade"),s) %>><%= s %></option>
                <%
                    }
                %>
            </select>
        </div>
        <%
            if (util.Seguranca.master(logado)) {
        %>
            <div>
                <label>Empresa</label>
                <select name="empresaId">
                    <option value="">Todas</option>
                    <%
                        for (Empresa e : (List<Empresa>) request.getAttribute("empresas")) {
                    %>
                        <option value="<%= e.getId() %>" <%= Web.selecionado(request.getParameter("empresaId"),e.getId()) %>><%= Web.html(e.getNome()) %></option>
                    <%
                        }
                    %>
                </select>
            </div>
        <%
            }
        %>
        <div>
            <label>Categoria</label>
            <select name="categoriaId">
                <option value="">Todas</option>
                <%
                    for (Categoria categoria : (List<Categoria>) request.getAttribute("categorias")) {
                %>
                    <option value="<%= categoria.getId() %>" <%= Web.selecionado(request.getParameter("categoriaId"),categoria.getId()) %>><%= Web.html(categoria.getNome()) %></option>
                <%
                    }
                %>
            </select>
        </div>
        <button>Filtrar</button>
    </form>
    <div class="tabela">
        <table>
            <thead>
                <tr>
                    <th>Protocolo / título</th>
                    <th>Categoria / setor</th>
                    <th>Empresa</th>
                    <th>Prioridade</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Chamado c : itens) {
                %>
                    <tr>
                        <td><strong><%= Web.html(c.getProtocolo()) %></strong><small><%= Web.html(c.getTitulo()) %></small></td>
                        <td><%= Web.html(c.getCategoria()) %><small><%= Web.html(c.getSetor()) %></small></td>
                        <td><%= Web.html(c.getEmpresaNome()) %></td>
                        <td><%= Web.html(c.getPrioridade()) %></td>
                        <td><span class="badge"><%= Web.html(c.getStatus()) %></span></td>
                        <td class="acoes">
                            <a href="<%= ctx %>/chamado-detalhes?id=<%= c.getId() %>">Detalhes</a>
                            <%
                                if ("Aberto".equals(c.getStatus())
                                        && (util.Seguranca.administrador(logado)
                                                || c.getSolicitanteId() == logado.getId())) {
                            %>
                                <a href="<%= ctx %>/chamados?acao=editar&id=<%= c.getId() %>">Editar</a>
                            <%
                                }
                            %>
                        </td>
                    </tr>
                <%
                    }
                %>
                <%
                    if (itens.isEmpty()) {
                %>
                    <tr>
                        <td class="vazio" colspan="6">Nenhum chamado encontrado.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
    <p><%= request.getAttribute("total") %> resultado(s). Página <%= request.getAttribute("pagina") %> de <%= request.getAttribute("paginas") %>.</p>
    <form class="acoes" method="get">
        <%
            for (String campo :
                    Arrays.asList("busca", "status", "prioridade", "empresaId", "categoriaId")) {
        %>
            <input type="hidden" name="<%= campo %>" value="<%= Web.html(request.getParameter(campo)) %>">
        <%
            }
        %>
        <label for="pagina">Página</label>
        <input id="pagina" name="pagina" type="number" min="1" max="<%= request.getAttribute("paginas") %>" value="<%= request.getAttribute("pagina") %>">
        <button class="secundario">Ir</button>
    </form>
</section>
<%@ include file="rodape.jspf" %>
