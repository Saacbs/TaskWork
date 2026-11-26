<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<p class="rotulo">CENTRAL DE ATENDIMENTO</p>
<h1>Notificações</h1>
<section class="card">
    <form method="post" action="<%= ctx %>/notificacoes">
        <%= util.Seguranca.campoCsrf(request) %>
        <button class="secundario" name="acao" value="todas">Marcar todas como lidas</button>
    </form>
    <ul class="lista">
        <%
            List<Notificacao> itens = (List<Notificacao>) request.getAttribute("itens");
            for (Notificacao n : itens) {
        %>
            <li>
                <div>
                    <strong><%= n.isLida()?"Lida":"Nova" %></strong>
                    <p><%= Web.html(n.getTexto()) %></p>
                    <small><%= Web.data(n.getCriadoEm()) %></small>
                    <%
                        if (n.getChamadoId() > 0) {
                    %>
                        <a href="<%= ctx %>/chamado-detalhes?id=<%= n.getChamadoId() %>">Ver chamado</a>
                    <%
                        }
                    %>
                </div>
                <%
                    if (!n.isLida()) {
                %>
                    <form method="post" action="<%= ctx %>/notificacoes">
                        <%= util.Seguranca.campoCsrf(request) %>
                        <input type="hidden" name="id" value="<%= n.getId() %>">
                        <button class="secundario">Marcar como lida</button>
                    </form>
                <%
                    }
                %>
            </li>
        <%
            }
            if (itens.isEmpty()) {
        %>
            <li>Nenhuma notificação.</li>
        <%
            }
        %>
    </ul>
</section>
<%@ include file="rodape.jspf" %>
