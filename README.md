# Cadastro de Clientes — Sistema Desktop em Java

Aplicação desktop desenvolvida em Java para controle de clientes, produtos e pedidos, feita para uso interno na empresa HALAK.

Foi um dos primeiros projetos "grandes" que desenvolvi. Hoje o código pode parecer simples, mas foi essencial pra entender na prática como estruturar uma aplicação em camadas, algo que só a teoria da aula não ensina.

## Funcionalidades

- Cadastro, edição, exclusão e busca de clientes (CRUD completo)
- Importação em massa de clientes via arquivo CSV
- Importação de clientes a partir de arquivo TXT com formato próprio, incluindo verificação de CNPJ duplicado
- Cadastro e controle de produtos, com leitura/escrita em arquivo TXT
- Registro de produtos associados a pedidos, com persistência em banco de dados
- Consulta e exclusão de produtos vinculados a um pedido específico

## Arquitetura

O projeto segue separação em camadas:

- **model** — classes de domínio (`Usuario`, `Produto`, `Pedido`)
- **dao** — acesso a dados (`ClienteDAO`, `ProdutoDAO`, `PedidoDAO`), usando JDBC com `PreparedStatement`
- **view** — telas Swing (`TelaCadastroCliente`, `TelaComprasCliente`, `TelaPedidosCliente`)
- **util** — classe de conexão com o banco (`Conexao`)

## Tecnologias

- Java (Swing para interface gráfica)
- JDBC + MySQL Connector/J
- JPA/EclipseLink (`persistence.xml`)
- NetBeans (estrutura de projeto Ant)

## Banco de dados

Conexão configurada via JDBC para um banco MySQL local (`bdhalak`). Para rodar o projeto, é necessário ter um servidor MySQL local com esse banco criado e as tabelas correspondentes (`cliente`, `compras`, entre outras usadas pelo DAO).

## Como rodar

1. Abrir o projeto no NetBeans (estrutura padrão Ant, `build.xml` incluso)
2. Configurar um banco MySQL local chamado `bdhalak`
3. Ajustar usuário/senha de conexão em `util/Conexao.java`, se necessário
4. Rodar a classe `view/TelaCadastroCliente.java` (tela inicial da aplicação)

## Aprendizados

Este projeto foi onde entendi na prática conceitos como separação de responsabilidades entre camadas, uso de `PreparedStatement` para evitar SQL Injection, e diferentes formas de importar dados externos (CSV e TXT) para dentro de um sistema.
