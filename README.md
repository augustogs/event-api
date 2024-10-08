# EventAPI

Instruções sobre como configurar e rodar a aplicação.

## Tecnologias utilizadas
- **Java com Spring Boot**
- **H2 Database**
- **JUnit**
- **Mockito**

## Requisitos

- **Java 18** ou superior instalado.
- **Docker** e **Docker Compose** instalados.

## Configuração

### 1. Clonar o Repositório

Primeiro, clone o repositório para sua máquina local:

```sh
git clone https://github.com/augustogs/event-api
```

### 2. Acesse a raiz do projeto e execute a construção e inicialização dos containers com Docker Compose:

Acesse a raiz do diretório do projeto no terminal e execute o seguinte comando para construir a imagem Docker e iniciar os containers:

```sh
docker-compose up --build
```

### 3. Testando os Endpoints

Após ter a aplicação em execução, você pode testar os endpoints utilizando ferramentas como **Insomnia** ou **Postman**.

A aplicação deverá estar sendo executada no caminho `http://localhost:8080`.



#### Criar um novo evento
**Endpoint:** `POST /api/events`

**Corpo da Requisição (JSON):**

```json
{
  "name": "Evento exemplo",
  "vacancies": 10,
  "startDate": "2024-08-10T23:00:00",
  "endDate": "2024-08-10T23:59:00"
}
```

#### Criar um novo usuário
**Endpoint:** `POST /api/users`

**Corpo da Requisição (JSON):**

```json
{
    "name": "Teste",
    "email": "teste@teste.com"
}
```

#### Inscrever usuário em evento
**Endpoint:** `POST api/events/registration`

```json
{
    "userEmail": "teste@teste.com",
    "eventId": 1
}
```

#### Cancelar inscrição de usuário em evento
**Endpoint:** `DELETE api/events/remove`

```json
{
    "userEmail": "teste@teste.com",
    "eventId": 1
}
```

#### Listar inscrições de um usuário
**Endpoint:** `GET api/events/user/{userId}`

Exemplo de requisição
```plaintext
GET /api/events/user/1
```

#### Listar incritos em um evento
**Endpoint:** `GET api/events/{eventId}`

Exemplo de requisição
```plaintext
GET /api/events/1
```
#### Realizar entrada de usuário em evento
**Endpoint:** `POST api/events/remove`

```json
{
    "userEmail": "teste@teste.com",
    "eventId": 1
}
```
#### Realizar reserva em evento
**Endpoint:** `POST api/events/reserve`

```json
{
    "userEmail": "teste@teste.com",
    "eventId": 1
}
```

#### Converter reserva em inscrição
**Endpoint:** `POST api/events/convert`

```json
{
    "reserveId": 1
}
```
