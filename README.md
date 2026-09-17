# TaskWork

Sistema web para abertura, acompanhamento e gerenciamento de chamados de suporte técnico em ambientes corporativos.

O **TaskWork** tem como objetivo centralizar solicitações de colaboradores, facilitar o trabalho da equipe de suporte e manter todo o histórico de atendimento organizado e acessível.

## Visão geral

O TaskWork foi planjado para substituir processos descentralizados, como pedidos de suporte enviados por mensagens, ligações ou planilhas. Como um dos principais objetivos, o TaskWork deve ser um sistema de chamados de facil manuseio. A aplicação reúne os chamados em um único ambiente e permite acompanhar cada solicitação desde a abertura até a resolução.

### Objetivos específicos

- Padronizar a abertura de solicitações de suporte.
- Permitir que o colaborador acompanhe seus próprios chamados.
- Oferecer à equipe administrativa uma visão completa da operação.
- Registrar alterações e interações no histórico de cada chamado.
- Disponibilizar indicadores para acompanhamento dos atendimentos.

## Perfis de acesso

| Perfil | Permissões principais |
|---|---|
| Visitante | Acessar a página pública, consultar indicadores gerais, visualizar categorias, consultar um chamado pelo número e acessar o login |
| Colaborador | Abrir chamados, consultar os próprios chamados, pesquisar solicitações e visualizar detalhes e histórico |
| Administrador | Visualizar todos os chamados, aplicar filtros, atualizar status, registrar observações, aprovar, recusar e concluir atendimentos |

## Funcionalidades do sistema

### 1. Portal público

- Apresentação do serviço e de seus benefícios.
-  Exibição de indicadores calculados com base nos chamados cadastrados.
-  Listagem das categorias de atendimento disponíveis.
-  Explicação resumida das etapas de atendimento.
-  Consulta pública de chamado pelo número de protocolo.
-  Seção de perguntas frequentes.
-  Navegação para a área de autenticação.
-  Conteúdo institucional administrável pelo backend.
-  Central de ajuda com artigos e pesquisa por assunto.

### 2. Autenticação e controle de acesso

-  Login com e-mail e senha utilizando contas fakes.
-  Opção para exibir ou ocultar a senha.
-  Preenchimento rápido das credenciais demonstrativas.
-  Redirecionamento conforme o perfil autenticado.
-  Proteção das páginas de colaborador e administrador por perfil.
-  Encerramento da sessão por meio da opção **Sair**.
-  Cadastro e ativação de novos usuários.
-  Autenticação real com senha criptografada.
-  Recuperação e redefinição de senha.
-  Controle de acesso baseado em papéis e permissões.
- Expiração segura da sessão e renovação de acesso.

### 3. Painel do colaborador

-  Mensagem de "bem vindo" com nome do usuário autenticado.
-  Indicadores dos chamados do colaborador: em aberto, em atendimento, aguardando contato e resolvidos.
-  Listagem dos chamados pertencentes ao colaborador.
- Pesquisa por número, título, categoria ou status.
-  Acesso rápido à abertura de um novo chamado.
-  Visualização detalhada da solicitação e de seu histórico.
- Paginação e ordenação dos resultados.
- Reabertura de chamados dentro de um prazo configurável.
- Avaliação do atendimento após a resolução.

### 4. Abertura de chamado

-  Formulário com nome, e-mail, empresa, telefone, categoria, título, descrição e prioridade.
-  Validação dos campos obrigatórios.
-  Validação de descrição com no mínimo 20 caracteres.
-  Geração automática de protocolo 
-  Definição automática do status inicial como **Não resolvido**.
-  Registro da data de criação e da primeira ocorrência no histórico.
-  Tela de confirmação com o número do chamado gerado.
- Inclusão de arquivos anexos, como imagens, documentos e evidências.
- Validação de formato e tamanho dos anexos.
- Sugestão de artigos da central de ajuda antes da abertura.
- Envio de confirmação por e-mail.

### 5. Consulta e detalhes do chamado

- Exibição do número, título, descrição, categoria, prioridade e status.
-  Exibição dos dados do solicitante, empresa e telefone.
- Exibição das datas de criação e última atualização.
- Histórico cronológico das movimentações do chamado.
- Exibição do motivo quando um chamado é recusado.
- Consulta pública por protocolo em modo somente leitura.
- Restrição para que o colaborador visualize somente seus próprios chamados.
- Comentários entre solicitante e equipe de suporte.
- Impressão ou exportação do atendimento em PDF.

### 6. Painel administrativo

- Indicadores de total, não resolvidos, em atendimento, aguardando contato e resolvidos.
- Listagem de todos os chamados cadastrados.
- Pesquisa por número, solicitante, título ou e-mail.
- Filtros por status, categoria e prioridade.
- Opção para limpar todos os filtros.
- Acesso aos detalhes de qualquer chamado.
- Início do atendimento.
- Solicitação de contato ou de informações adicionais.
- Envio do chamado para aprovação.
- Aprovação e retorno do chamado ao atendimento.
- Registro de observações da equipe de suporte.
- Resolução do chamado.
- Recusa com preenchimento obrigatório do motivo.
- Atualização automática da data e do histórico a cada ação.
- Atribuição do chamado a um atendente ou equipe.
- Transferência entre setores.
- Controle de prazo e nível de serviço (SLA).
- Identificação visual de chamados atrasados.
- Relatórios por período, categoria, prioridade, status e atendente.
- Exportação de relatórios em CSV e PDF.

### 7. Histórico e auditoria

- Registro das principais mudanças ocorridas no chamado.
- Identificação do autor, data e descrição da ocorrência.
- Ordenação do histórico do evento mais recente para o mais antigo.
- Registro imutável de auditoria no backend.
- Armazenamento de valores anteriores e posteriores nas alterações críticas.
- Consulta administrativa de atividades por usuário e período.

## Fluxo de um chamado

1. O colaborador autentica-se no sistema.
2. Preenche o formulário e envia uma nova solicitação.
3. O TaskWork gera um número de protocolo e registra o chamado como **Não resolvido**.
4. Um administrador inicia o atendimento.
5. Durante a análise, o chamado pode aguardar contato ou aprovação.
6. Após o atendimento, o administrador resolve ou recusa a solicitação.
7. Todas as movimentações são incluídas no histórico.
8. O colaborador acompanha o resultado pelo painel ou pelo número do protocolo.

### Estados dos chamados

| Status | Descrição |
|---|---|
| Não resolvido | Chamado criado e aguardando o início do atendimento |
| Em atendimento | Solicitação em análise pela equipe de suporte |
| Aguardando contato | Atendimento depende de informações ou retorno do solicitante |
| Aguardando aprovação | A execução depende da aprovação de uma pessoa responsável |
| Resolvido | Solicitação concluída com sucesso |
| Recusado | Solicitação não atendida, acompanhada de justificativa obrigatória |

## Regras de negócio

|  | Regra |
|---|---|
|  | Cada chamado deve possuir um número de protocolo único |
| | Todo chamado deve estar associado a um solicitante e a uma categoria |
|  | A descrição deve possuir pelo menos 20 caracteres |
|  | Um novo chamado deve iniciar com o status **Não resolvido** |
|  | O colaborador pode visualizar somente os próprios chamados |
|  | Somente administradores podem executar ações administrativas |
|  | A recusa exige o registro de uma justificativa |
|  | Toda alteração relevante deve gerar uma ocorrência no histórico |
|  | A data de atualização deve ser modificada a cada movimentação |
|  | A consulta pública deve apresentar somente informações não sensíveis |
|  | A prioridade deve ser **Baixa**, **Média**, **Alta** ou **Crítica** |
|  | Categorias e usuários inativos não podem ser utilizados em novos chamados |

### Categorias iniciais

- Instalação de software
- Problema de acesso
- Equipamento
- Rede ou internet
- E-mail
- Sistemas internos
- Solicitação de equipamento
- Outros

## Modelo de dados

O modelo relacional planejado utiliza as seguintes entidades principais:

- **Empresa:** organização à qual os usuários pertencem.
- **Usuário:** colaborador ou administrador que acessa o sistema.
- **Categoria:** classificação utilizada na abertura do chamado.
- **Chamado:** solicitação principal, com protocolo, descrição, status e prioridade.
- **Histórico:** registro de todas as movimentações e observações do chamado.
- **Anexo:** arquivo relacionado a um chamado ou a uma ocorrência.
- **Avaliação:** nota e comentário do solicitante após a conclusão.

## Referenciamento

### Protótipos das telas

#### 1. Página inicial
[Visualizar página inicial](Imagens/1-Pagina%20inicial%20%28Não%20logavel%29.png)

#### 2. Login
[Visualizar tela de login](Imagens/2-%20Login.png)

#### 3. Painel do colaborador
[Visualizar painel do colaborador](Imagens/3-%20painel%20do%20colaborador.png)

#### 3.1. Menu de notificações
[Visualizar menu de notificações](Imagens/03.01%20%E2%80%94%20Painel%20do%20colaborador%20pós%20abertura%20de%20menu%20de%20notificações.png)

#### 4. Novo chamado
[Visualizar tela de novo chamado](Imagens/04%20%E2%80%94%20Novo%20chamado.png)

#### 4.1. Novo chamado após abertura
[Visualizar chamado após abertura](Imagens/04.01%20%E2%80%94%20Novo%20chamado%20após%20abertura%20de%20chamado.png)

#### 5. Detalhes do chamado
[Visualizar detalhes do chamado](Imagens/05%20%E2%80%94%20Detalhes%20do%20chamado.png)

#### 6. Painel administrativo
[Visualizar painel administrativo](Imagens/06%20%E2%80%94%20Painel%20administrativo.png)

#### 7. Gestão do chamado
[Visualizar gestão do chamado](Imagens/07%20%E2%80%94%20Gestão%20do%20chamado.png)

#### 7.1. Opção de cancelamento
[Visualizar tela de cancelamento](Imagens/07.01%20%E2%80%94%20Tela%20pós%20opção%20de%20cancelamento.png)

#### 8. Relatórios
[Visualizar relatórios](Imagens/08%20%E2%80%94%20Relatórios.png)

#### 9. Cadastros
[Visualizar cadastros](Imagens/09%20%E2%80%94%20Cadastros.png)

#### 10. Auditoria
[Visualizar auditoria](Imagens/10%20%E2%80%94%20Auditoria.png)

### Modelo de dados

#### Diagrama Entidade-Relacionamento
[Visualizar diagrama entidade-relacionamento](Imagens/Diagrama%20entidade%20relacionamento.png)

#### Cardinalidades
[Visualizar cardinalidades](Imagens/Cardinalidades.png)
#### Cardinalidades

![Cardinalidades](Images/Cardinalidades.png)

  

  

