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

- Nossa API **TaskManagerAPI** é um sistema de gerenciamento de tarefas que permite a autenticação de usuários e o controle de tarefas atribuídas a cada um. Seu principal objetivo é facilitar o gerenciamento pessoal ou colaborativo de atividades, oferecendo funcionalidades como:

- Cadastro e login de usuários
- Criação, leitura, atualização e exclusão (CRUD) de tarefas
- Validação de autenticação via JWT

---

## 2. Decisões Arquiteturais

### Arquitetura Utilizada
- Arquitetura em camadas, adotada por sua clareza e boa separação de responsabilidades.
- Uso de DTOs para isolar a lógica de negócio das representações expostas pela API.

### Justificativas
- Esta estrutura foi escolhida por ser adequada para projetos com pouca complexidade e academicos e por já estar alinhada com nosso conhecimento prévio.
- **Spring Boot**: utilizado por sua facilidade e eficiencia no desenvolvimento de APIs e familiaridade prévia com o framework.
- **JWT**: escolhido para experimentar autenticação baseada em token de forma simples, sem a complexidade de sessões.

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
#### Request Body:
```json
    {
      "email": "user@email.com",
      "password": "password"
    }
```
####
### 👤 Usuários
- `POST /users`: Cria novo usuário
#### Request Body:
 ```json
    {
      "firstName": "name",
      "lastName": "lastName",
      "email": "user@email.com",
      "password": "password"
    }
 ```
- `GET /users/{id}`: Retorna detalhes de um usuário específico
####
- `PUT /users/{id}`: Atualiza um usuário
#### Request Body:
 ```json
    {
      "firstName": "name",
      "lastName": "lastName",
      "email": "user@email.com",
      "password": "password"
    }
 ```
####
- `DELETE /users/{id}`: Remove um usuário (soft delete)

### ✅ Tarefas
- `POST /tasks`: Cria nova tarefa
#### Request Body:
 ```json
    {
      "firstName": "name",
      "lastName": "lastName",
      "email": "user@email.com",
      "password": "password"
    }
 ```
####
- `GET /tasks?assignedTo=id`: Lista todas as tarefas do usuário com id indicado no campo `assignedTo`
####
- `GET /tasks/{id}`: Retorna detalhes de uma tarefa específica
####
- `PUT /tasks/{id}`: Atualiza uma tarefa
#### Request Body:
 ```json
    {
      "title": "title",
      "description": "description",
      "status": "status",
      "assignedTo": 1
    }
 ```

- `DELETE /tasks/{id}`: Remove uma tarefa

---

## 5. Configuração e Deploy

### 📁 Dependências
- Java 17+
- Spring Boot 3+
- Maven
- PostgreSQL 

### ⚙️ Configuração do Banco de Dados

Antes de rodar a aplicação, **é necessário criar o banco de dados no PostgreSQL manualmente**.

1. Crie o banco com o nome `taskdb` usando o cliente `psql` ou outra ferramenta, por exemplo:

```sql
CREATE DATABASE taskdb;
```

2. Crie um arquivo `application-local.yaml` (dentro da pasta `resources`), configure o acesso ao banco, informando o usuário e a senha do seu PostgreSQL:

```yaml
spring:
    datasource:
        username: SEU_USUARIO
        password: SUA_SENHA
```

Assim, a aplicação conseguirá se conectar ao banco corretamente.

### ▶️ Execução

Acesse a pasta do projeto `taskmanagerAPI` e execute os seguintes comandos:

```bash
# Build do projeto
mvn clean install

# Executar aplicação
mvn spring-boot:run
```

---

## 6. Testes Automatizados

### 🧪 Execução de Testes

```bash
# Executar todos os testes
mvn test
```

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
