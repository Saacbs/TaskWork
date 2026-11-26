<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    Resumo resumo = (Resumo) request.getAttribute("resumo");
    List<Chamado> itens = (List<Chamado>) request.getAttribute("itens");
%>
<div class="titulo-acoes">
    <div>
        <p class="rotulo">ADMINISTRAÇÃO</p>
        <h1>Relatórios</h1>
        <p>Consulte os chamados pelo período de criação, dentro do seu acesso.</p>
    </div>
    <button class="secundario" type="button" data-imprimir>Imprimir relatório</button>
</div>
<section class="card">
    <h2>Filtros do relatório</h2>
    <form class="filtros" method="get">
        <div>
            <label>Data inicial</label>
            <input name="inicio" type="date" value="<%= Web.html(request.getParameter("inicio")) %>">
        </div>
        <div>
            <label>Data final</label>
            <input name="fim" type="date" value="<%= Web.html(request.getParameter("fim")) %>">
        </div>
        <div>
            <label>Status</label>
            <select name="status">
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
        <div>
            <label>Responsável</label>
            <select name="responsavelId">
                <option value="">Todos</option>
                <%
                    for (Usuario pessoa : (List<Usuario>) request.getAttribute("atendentes")) {
                %>
                    <option value="<%= pessoa.getId() %>" <%= Web.selecionado(request.getParameter("responsavelId"),pessoa.getId()) %>><%= Web.html(pessoa.getNome()) %></option>
                <%
                    }
                %>
            </select>
        </div>
        <button>Aplicar filtros</button>
        <a href="<%= ctx %>/relatorios">Limpar</a>
    </form>
</section>
<div class="indicadores quatro">
    <article class="card indicador">
        <span>Chamados resolvidos</span>
        <strong><%= resumo.getResolvidos() %></strong>
    </article>
    <article class="card indicador">
        <span>Dentro do SLA</span>
        <strong><%= resumo.getSla() %></strong>
    </article>
    <article class="card indicador">
        <span>Tempo médio de resolução</span>
        <strong><%= resumo.getTempo() %></strong>
    </article>
    <article class="card indicador">
        <span>Satisfação média</span>
        <strong><%= resumo.getSatisfacao() %></strong>
    </article>
</div>
<p class="ajuda">SLA em horas corridas: crítica 4 h, alta 8 h, média 24 h e baixa 48 h. SLA e tempo médio usam somente resolvidos com datas conhecidas (<%= resumo.getComPrazo() %>). Satisfação usa avaliações enviadas pelo solicitante.</p>
<section class="card">
    <h2>Chamados do recorte (<%= resumo.getTotal() %>)</h2>
    <div class="tabela">
        <table>
            <thead>
                <tr>
                    <th>Chamado</th>
                    <th>Empresa</th>
                    <th>Solicitante</th>
                    <th>Status</th>
                    <th>Abertura</th>
                    <th>Resolução</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Chamado c : itens) {
                %>
                    <tr>
                        <td><a href="<%= ctx %>/chamado-detalhes?id=<%= c.getId() %>"><%= Web.html(c.getProtocolo()) %> — <%= Web.html(c.getTitulo()) %></a></td>
                        <td><%= Web.html(c.getEmpresaNome()) %></td>
                        <td><%= Web.html(c.getSolicitanteNome()) %></td>
                        <td><%= Web.html(c.getStatus()) %></td>
                        <td><%= Web.data(c.getCriadoEm()) %></td>
                        <td><%= Web.data(c.getResolvidoEm()) %></td>
                    </tr>
                <%
                    }
                    if (itens.isEmpty()) {
                %>
                    <tr>
                        <td colspan="6" class="vazio">Nenhum chamado encontrado.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="rodape.jspf" %>
