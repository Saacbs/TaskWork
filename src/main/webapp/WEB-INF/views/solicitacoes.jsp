<%@ page pageEncoding="UTF-8" %>
<%@ include file="cabecalho.jspf" %>
<p class="rotulo">ADMINISTRAÇÃO</p>
<h1>Solicitações de cadastro</h1>
<section class="card">
    <p>Analise os pedidos dentro das empresas do seu acesso.</p>
    <div class="tabela">
        <table>
            <thead>
                <tr>
                    <th>Solicitante</th>
                    <th>Empresa / contato</th>
                    <th>Perfil</th>
                    <th>Situação</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<SolicitacaoCadastro> itens = (List<SolicitacaoCadastro>) request.getAttribute("itens");
                    for (SolicitacaoCadastro s : itens) {
                %>
                    <tr>
                        <td><strong><%= Web.html(s.getNome()) %></strong><small><%= Web.html(s.getEmail()) %></small></td>
                        <td><%= Web.html(s.getEmpresaNome()) %><small><%= Web.html(s.getTelefone()) %></small></td>
                        <td><%= Web.html(s.getPerfil()) %></td>
                        <td><%= Web.html(s.getSituacao()) %></td>
                        <td>
                            <%
                                if ("Pendente".equals(s.getSituacao())) {
                            %>
                                <form method="post" action="<%= ctx %>/solicitacoes" class="acoes">
                                    <%= util.Seguranca.campoCsrf(request) %>
                                    <input type="hidden" name="id" value="<%= s.getId() %>">
                                    <button name="acao" value="aprovar">Aprovar</button>
                                    <button class="secundario" name="acao" value="rejeitar">Rejeitar</button>
                                </form>
                            <%
                                }
                            %>
                        </td>
                    </tr>
                <%
                    }
                    if (itens.isEmpty()) {
                %>
                    <tr>
                        <td class="vazio" colspan="5">Nenhuma solicitação encontrada.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</section>
<%@ include file="rodape.jspf" %>
