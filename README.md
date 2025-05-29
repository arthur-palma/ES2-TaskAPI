# 📋 Documentação da API - TaskManagerAPI

## 📚 Sumário

1. [Visão Geral](#1-visão-geral)
2. [Decisões Arquiteturais](#2-decisões-arquiteturais)
3. [Modelagem de Dados](#3-modelagem-de-dados)
4. [Fluxo de Requisições](#4-fluxo-de-requisições)
5. [Configuração e Deploy](#5-configuração-e-deploy)
6. [Testes Automatizados](#6-testes-automatizados)
7. [Documentação da API com Swagger](#7-documentação-da-api-com-swagger)


---

## 1. Visão Geral

A API **TaskManagerAPI** é um sistema de gerenciamento de tarefas que permite a autenticação de usuários e o controle de tarefas atribuídas a cada um. Seu principal objetivo é facilitar o gerenciamento pessoal ou colaborativo de atividades, oferecendo funcionalidades como:

- Cadastro e login de usuários
- Criação, leitura, atualização e exclusão (CRUD) de tarefas
- Validação de autenticação via JWT

---

## 2. Decisões Arquiteturais

### Arquitetura Utilizada
- Padrão em camadas (Controller-Service-Repository)
- Separação entre interfaces e implementações nos serviços

### Justificativas
- **Spring Boot**: rápido para prototipagem e escalável para produção
- **JWT**: oferece segurança nas requisições sem sobrecarregar o servidor com sessões
- **Beans com `@Service` e `@Repository`**: boa organização e injeção de dependência nativa

---

## 3. Modelagem de Dados

### Entidade `User`
- `id`: int
- `firstName`: String
- `lastName`: String
- `email`: String
- `password`: String
- `active`: boolean
- `tasks`: Relacionamento `@OneToMany` com `Task`

### Entidade `Task`
- `id`: int
- `title`: String
- `description`: String
- `status`: String
- `user`: Relacionamento `@ManyToOne` com `User`

---

## 4. Fluxo de Requisições

### 🔐 Autenticação
- `POST /auth/login`: Login com e-mail e senha (retorna token)

### 👤 Usuários
- `POST /users`: Cria novo usuário
- `GET /users/{id}`: Retorna detalhes de um usuário específico
- `PUT /users/{id}`: Atualiza um usuário
- `DELETE /users/{id}`: Remove um usuário (soft delete)

### ✅ Tarefas
- `POST /tasks`: Cria nova tarefa
- `GET /tasks`: Lista todas as tarefas do usuário indicado no campo `assignedTo`
- `GET /tasks/{id}`: Retorna detalhes de uma tarefa específica
- `PUT /tasks/{id}`: Atualiza uma tarefa
- `DELETE /tasks/{id}`: Remove uma tarefa

---

## 5. Configuração e Deploy

### 📁 Dependências
- Java 17+
- Spring Boot 3+
- Maven
- PostgreSQL ou banco relacional compatível com JPA

### ⚙️ Configuração do Banco de Dados

Antes de rodar a aplicação, **é necessário criar o banco de dados no PostgreSQL manualmente**.

1. Crie o banco com o nome `taskdb` usando o cliente `psql` ou outra ferramenta, por exemplo:

```sql
CREATE DATABASE taskdb;
```

2. No arquivo `application-local.properties` (dentro da pasta `resources`), configure o acesso ao banco, informando o usuário e a senha do seu PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

Assim, a aplicação conseguirá se conectar ao banco corretamente.

### ▶️ Execução

Acesse a pasta do projeto `taskmanagerAPI` e execute os seguintes comandos:

```bash
# Build do projeto
mvn clean install

# Executar aplicação com o perfil local
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## 6. Testes Automatizados

### 🧪 Execução de Testes

```bash
# Executar todos os testes
mvn test
```

### 🧪 Relatório de Cobertura (opcional)

Se estiver usando **JaCoCo**, gere o relatório com:

```bash
mvn clean verify
```

O relatório estará disponível em:  
`target/site/jacoco/index.html`

### Estratégia

Foram utilizados **testes unitários**, distribuídos em três camadas da aplicação:

- `controller`: validações de comportamento dos endpoints e tratamento de exceções
- `mapper`: testes de conversão entre entidades e DTOs
- `service`: lógica de negócio e chamadas a repositórios com mocks

> ⚠️ Não foram implementados testes de integração.

### Frameworks e Técnicas
- **JUnit 5**
- **Mockito** para mocks

---
## 7. Documentação da API com Swagger

A documentação interativa da API está disponível via Swagger UI, facilitando a visualização dos endpoints e testes diretamente pela interface web.

### 🔍 Acessando o Swagger

Após subir a aplicação localmente, acesse:

```
http://localhost:8080/swagger-ui/index.html
```
---