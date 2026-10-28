<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<section class="landing-hero" id="como-funciona">
    <div class="landing-texto">
        <p class="selo-landing">●&nbsp; Suporte simples, rápido e transparente</p>
        <h1>Seu suporte<br>técnico,<br>sem complicação.</h1>
        <p>Centralize chamados, acompanhe cada etapa e encontre soluções com mais agilidade.</p>
        <div class="landing-acoes"><a class="botao" href="<%= ctx %>/login">Abrir um chamado →</a><a class="botao secundario" href="#consulta">Consultar chamado</a></div>
        <div class="landing-beneficios"><span>✓ Protocolo único</span><span>✓ Histórico completo</span><span>✓ Atualizações claras</span></div>
    </div>
    <section class="preview-chamado" aria-label="Exemplo de chamado">
        <div class="pontos-preview"><i></i><i></i><i></i></div>
        <div class="preview-cabecalho"><strong>#CH-2026-00125<br>Instalação do pacote Office</strong><span>Em atendimento</span></div>
        <div class="preview-etapas"><b>✓ Aberto</b><b>2. Atendimento</b><b>3. Resolvido</b></div>
        <div class="preview-mensagem"><strong>AS</strong><p>Equipe de suporte<br><small>Já iniciamos a análise da sua solicitação.</small></p></div>
    </section>
</section>
<section class="landing-atalhos" id="categorias"><a href="#consulta"><small>CONSULTA PÚBLICA</small><strong>Digite o protocolo →</strong></a><a href="#categorias"><small>CATEGORIAS</small><strong>3 tipos de suporte</strong></a><a href="#como-funciona"><small>COMO FUNCIONA</small><strong>3 etapas simples</strong></a><a href="#duvidas"><small>DÚVIDAS FREQUENTES</small><strong>Acessar FAQ →</strong></a></section>
<section id="consulta" class="consulta-publica card"><p class="rotulo">CONSULTA PÚBLICA</p><h2>Consultar chamado</h2><p>Digite o protocolo para acompanhar a solicitação.</p><form class="consulta-form" action="<%= ctx %>/chamado-detalhes"><input id="protocolo" placeholder="CH-2026-000001" required maxlength="40"><button type="submit">Consultar</button></form></section>
<section id="duvidas" class="duvidas-publicas"><h2>Dúvidas frequentes</h2><div><article><strong>Como abro um chamado?</strong><p>Acesse o portal, entre com sua conta e selecione “Novo chamado”.</p></article><article><strong>Não tenho acesso ao portal.</strong><p>Use “Solicitar cadastro” e aguarde a aprovação da empresa.</p></article></div></section>
<%@ include file="rodape.jspf" %>
