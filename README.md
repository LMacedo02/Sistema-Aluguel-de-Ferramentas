# AlugaTech

O **AlugaTech** é uma plataforma web para aluguel de ferramentas, desenvolvida com o objetivo de facilitar o acesso a equipamentos para profissionais da construção civil, pequenas empreiteiras, profissionais de manutenção e pessoas que realizam projetos DIY.

A proposta do sistema é permitir que o usuário alugue ferramentas específicas por um período determinado, evitando os altos custos de compra, manutenção, armazenamento e depreciação desses equipamentos.

---

## Sobre o Projeto

No cenário atual, muitos profissionais e entusiastas precisam utilizar ferramentas específicas apenas em situações pontuais. Comprar esses equipamentos pode ser caro e pouco viável, principalmente quando o uso é esporádico.

O AlugaTech surge como uma solução para esse problema, oferecendo uma plataforma simples, intuitiva e eficiente para aluguel de ferramentas.

---

## Público-Alvo

O projeto é voltado principalmente para:

- Construtores autônomos
- Pequenas empreiteiras
- Profissionais de manutenção
- Eletricistas, encanadores e marceneiros
- Hobbistas e entusiastas de projetos DIY

---

## Funcionalidades

### Usuário

- Cadastro de usuário
- Login e autenticação
- Visualização do catálogo de ferramentas
- Visualização dos detalhes de uma ferramenta
- Adição de ferramentas ao carrinho
- Definição da quantidade de dias de aluguel
- Visualização do carrinho
- Remoção de itens do carrinho
- Finalização do aluguel
- Escolha do método de pagamento
- Aplicação de desconto para pagamento via PIX
- Aplicação de desconto para aluguéis de 5 dias ou mais
- Visualização do histórico de aluguéis
- Devolução de ferramentas alugadas

### Administrador

- Acesso ao painel administrativo
- Gerenciamento de ferramentas
- Cadastro de novas ferramentas
- Edição de ferramentas existentes
- Exclusão de ferramentas do catálogo

---

## Regras de Negócio

- O usuário pode possuir no máximo **3 aluguéis pendentes**.
- Aluguéis com **5 dias ou mais** recebem desconto de **5%**.
- Pagamentos realizados via **PIX** recebem desconto adicional de **5%**.
- Todo aluguel iniciado recebe o status **PENDENTE**.
- Após a devolução, o aluguel passa para o status **FINALIZADO**.
- Apenas usuários com perfil **ADMIN** podem gerenciar ferramentas.

---

## Tecnologias Utilizadas

### Frontend

- HTML5
- CSS3
- Bootstrap 5
- Thymeleaf
- JavaScript

### Backend

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- H2 Database
- Maven

---

## Arquitetura do Sistema

O projeto segue uma arquitetura em camadas baseada no padrão MVC.

### Camadas principais

- **Controller:** responsável por receber as requisições HTTP e controlar o fluxo da aplicação.
- **Service:** responsável pelas regras de negócio, como descontos, validações e limite de aluguéis.
- **Repository:** responsável pela comunicação com o banco de dados.
- **Model/Entity:** representa as entidades do sistema, como Usuário, Ferramenta e Aluguel.
- **View:** páginas HTML renderizadas com Thymeleaf.

---

## Principais Entidades

- **Usuário:** representa os clientes e administradores do sistema.
- **Ferramenta:** representa os equipamentos disponíveis para aluguel.
- **Carrinho:** armazena temporariamente os itens escolhidos pelo usuário.
- **ItemCarrinho:** representa uma ferramenta adicionada ao carrinho com a quantidade de dias.
- **Aluguel:** registra os dados do aluguel realizado.
- **Pagamento:** representa o método de pagamento e os valores aplicados.

---

## Fluxo Principal de Aluguel

1. O usuário acessa o catálogo de ferramentas.
2. Seleciona uma ferramenta.
3. Informa a quantidade de dias de aluguel.
4. Adiciona a ferramenta ao carrinho.
5. Visualiza o carrinho com subtotal, descontos e total.
6. Escolhe o método de pagamento.
7. O sistema valida o limite de aluguéis pendentes.
8. O sistema aplica os descontos, se necessário.
9. O aluguel é registrado com status **PENDENTE**.
10. Após a devolução, o aluguel é atualizado para **FINALIZADO**.

---

## Segurança

A segurança da aplicação é feita com **Spring Security**.

O sistema possui controle de acesso baseado em perfis:

- Usuários comuns acessam funcionalidades de aluguel.
- Administradores acessam funcionalidades de gerenciamento de ferramentas.

Rotas administrativas são protegidas para que apenas usuários com perfil **ADMIN** possam acessá-las.

---

## Banco de Dados

O projeto utiliza o banco de dados **H2 Database**, configurado em modo arquivo para manter os dados salvos mesmo após reiniciar a aplicação.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:h2:file:./data/alugatech
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
