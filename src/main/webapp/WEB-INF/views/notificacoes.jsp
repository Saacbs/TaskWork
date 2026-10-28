<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<p class="rotulo">CENTRAL DE ATENDIMENTO</p><div class="titulo-acoes"><div><h1>Notificações</h1><p>Acompanhe as atualizações dos seus chamados.</p></div><button class="secundario" type="button">Marcar todas como lidas</button></div>
<section class="card"><ul class="lista notificacoes"><li class="nao-lida"><span class="badge">Nova</span><a href="<%= ctx %>/chamado-detalhes">Seu chamado CH-2026-00125 está em atendimento.</a><small>Hoje, 10:30</small></li><li><span class="badge">Lida</span><a href="<%= ctx %>/chamado-detalhes">Uma atualização foi registrada no seu chamado.</a><small>Ontem, 15:20</small></li></ul></section>
<%@ include file="rodape.jspf" %>
