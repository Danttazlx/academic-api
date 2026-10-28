# Academic API

Projeto de estudo com Java e Spring Boot para gerenciar alunos, cursos e matrículas, utilizando Spring Security para autenticação.

## Tecnologias

Java 17+, Spring Boot, Spring Security, Spring Data JPA, Bean Validation, PostgreSQL, Lombok e Maven Wrapper.

## Executar

Tenha um JDK configurado em `JAVA_HOME` e um PostgreSQL disponível. Crie o banco `academic` e configure no PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/academic'
$env:POSTGRES_USER = 'seu_usuario'
$env:POSTGRES_PASSWORD = 'sua_senha'
$env:SPRING_DOCKER_COMPOSE_ENABLED = 'false'
./mvnw.cmd spring-boot:run
```

A API fica em `http://localhost:8080`. O Hibernate cria ou atualiza as tabelas.

O projeto também inclui `docker-compose.yml` com PostgreSQL na porta `5431` e pgAdmin na porta `5050`. A configuração acima usa um banco externo e desativa a inicialização automática do Compose.

## Autenticação

Todas as rotas exigem autenticação. No Postman, selecione **Basic Auth** e use uma das contas de estudo:

| Usuário | Senha | Perfil |
| --- | --- | --- |
| `user` | `123` | USER |
| `admin` | `321` | ADMIN |

As contas ficam em memória, com senhas codificadas por BCrypt. Ambos os perfis podem acessar o CRUD. Também é possível entrar pelo formulário em `/login`. A configuração atual mantém CSRF desativado.

## Endpoints

| Recurso | Rota |
| --- | --- |
| Alunos | `/v1/school/alunos` |
| Cursos | `/v1/school/cursos` |
| Matrículas | `/v1/school/matriculas` |

Para cada recurso: `POST` cria, `GET` lista, `GET /{id}` consulta, `PUT /{id}` atualiza e `DELETE /{id}` exclui.

Exemplo para criar um aluno com `POST /v1/school/alunos` e `Content-Type: application/json`:

```json
{
  "nome": "Ana Silva",
  "dataNascimento": "2000-01-15"
}
```

## Testes

```powershell
./mvnw.cmd test
```

O teste de inicialização precisa do banco configurado. Não versione credenciais: `.env` e `.env.*` estão ignorados pelo Git, exceto `.env.example`. Para executar pelo terminal, forneça as variáveis de ambiente como no exemplo acima.
