<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<%
    Resumo resumo = (Resumo) request.getAttribute("resumo");
    List<Chamado> itens = (List<Chamado>) request.getAttribute("itens");
%>
<div class="titulo-acoes">
    <div>
        <p class="rotulo">CENTRAL DE ATENDIMENTO</p>
        <h1>Meu painel</h1>
        <p>Monitore e acompanhe as solicitações do seu acesso.</p>
    </div>
    <a class="botao" href="<%= ctx %>/chamados?acao=novo">+ Novo chamado</a>
</div>
<div class="indicadores">
    <article class="card indicador">
        <span>Total</span>
        <strong><%= resumo.getTotal() %></strong>
    </article>
    <article class="card indicador">
        <span>Abertos</span>
        <strong><%= resumo.getAbertos() %></strong>
    </article>
    <article class="card indicador">
        <span>Em atendimento</span>
        <strong><%= resumo.getEmAtendimento() %></strong>
    </article>
    <article class="card indicador">
        <span>Aguardando</span>
        <strong><%= resumo.getAguardando() %></strong>
    </article>
    <article class="card indicador">
        <span>Resolvidos</span>
        <strong><%= resumo.getResolvidos() %></strong>
    </article>
</div>
<p class="ajuda">Cancelados: <%= resumo.getCancelados() %> · Recusados: <%= resumo.getRecusados() %></p>
<section class="card">
    <h2>Chamados recentes</h2>
    <p>Os dez chamados mais recentes dentro do seu acesso.</p>
    <a href="<%= ctx %>/chamados">Ver todos e filtrar</a>
    <div class="tabela">
        <table>
            <thead>
                <tr>
                    <th>Protocolo / título</th>
                    <th>Solicitante</th>
                    <th>Categoria</th>
                    <th>Prioridade</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Chamado c : itens) {
                %>
                    <tr>
                        <td><a href="<%= ctx %>/chamado-detalhes?id=<%= c.getId() %>"><%= Web.html(c.getProtocolo()) %></a><small><%= Web.html(c.getTitulo()) %></small></td>
                        <td><%= Web.html(c.getSolicitanteNome()) %></td>
                        <td><%= Web.html(c.getCategoria()) %></td>
                        <td><%= Web.html(c.getPrioridade()) %></td>
                        <td><span class="badge"><%= Web.html(c.getStatus()) %></span></td>
                    </tr>
                <%
                    }
                %>
                <%
                    if (itens.isEmpty()) {
                %>
                    <tr>
                        <td colspan="5" class="vazio">Nenhum chamado encontrado.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="rodape.jspf" %>
