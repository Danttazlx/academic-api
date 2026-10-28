# Academic API — cadastro e login

API REST para cadastrar alunos e autenticar por e-mail e senha. O projeto mantém as camadas `controller`, `service`, `repository`, `domain`, `dto`, `config` e tratamento de erros.

Os CRUDs de alunos, cursos e matrículas foram removidos. Não existem contas fixas nem perfil de administrador. Permanecem cadastro, login e os recursos de segurança necessários à sessão: CSRF e logout.

## Tecnologias

Java 17 ou superior, Spring Boot 4.1.1, Spring MVC, Spring Security, Spring Data JPA, Bean Validation, Lombok e PostgreSQL. Maven Wrapper incluído. Os testes usam H2 em memória.

## Executar

Instale um JDK e disponibilize um banco PostgreSQL local ou remoto. Crie o banco `academic` e um usuário com permissão para criar e atualizar suas tabelas. Docker e Docker Compose não são necessários. No PowerShell, na raiz do projeto:

```powershell
$env:JAVA_HOME = 'C:/Program Files/Java/jdk-21.0.11' # ajuste para seu JDK
$env:POSTGRES_USER = 'academic'
$env:POSTGRES_PASSWORD = '<sua-senha-local>'
$env:DB_URL = 'jdbc:postgresql://localhost:5432/academic'
./mvnw.cmd spring-boot:run
```

Base: `http://localhost:8080`. As variáveis devem estar definidas no terminal que inicia a aplicação. Não há interface gráfica: utilize Postman ou outro cliente HTTP.

O PostgreSQL é gerenciado fora da aplicação. Ajuste `DB_URL`, `POSTGRES_USER` e `POSTGRES_PASSWORD` para seu ambiente. O Hibernate mantém `ddl-auto: update`; a remoção das classes de cursos e matrículas não apaga as tabelas antigas do banco. A remoção do Compose não exclui bancos, contêineres ou volumes existentes.

## Endpoints

| Método | Rota | Resultado |
| --- | --- | --- |
| GET | `/v1/auth/csrf` | Token CSRF para a sessão |
| POST | `/v1/auth/cadastro` | Cadastra aluno; retorna `201` com ID, nome e nascimento |
| POST | `/v1/auth/login` | Autentica; retorna `204` e atualiza a sessão |
| POST | `/v1/auth/logout` | Invalida a sessão; retorna `204` |

Login e logout são processados pelo Spring Security. Rotas fora desse conjunto não estão disponíveis.

## Usar no Postman

Mantenha o armazenamento de cookies habilitado durante toda a sequência.

### 1. Obter token

Envie `GET http://localhost:8080/v1/auth/csrf`. A resposta contém `token`, `headerName` e `parameterName`. Guarde o cookie `JSESSIONID` e envie o token no cabeçalho indicado por `headerName`, normalmente `X-CSRF-TOKEN`, em todos os POSTs.

### 2. Cadastrar

Envie `POST http://localhost:8080/v1/auth/cadastro`, com o cabeçalho CSRF e Body → raw → JSON:

```json
{
  "nome": "Ana Silva",
  "dataNascimento": "2000-01-15",
  "email": "ana@example.com",
  "senha": "minha-senha-forte-123"
}
```

Nome, nascimento, e-mail válido e senha são obrigatórios. A senha deve ter entre 12 e 72 caracteres e no máximo 72 bytes UTF-8. O e-mail é normalizado para minúsculas e deve ser único. A senha é armazenada como hash BCrypt e não é retornada.

Resposta de sucesso (`201`):

```json
{ "id": 1, "nome": "Ana Silva", "dataNascimento": "2000-01-15" }
```

O cadastro não inicia a sessão autenticada automaticamente.

### 3. Entrar

Envie `POST http://localhost:8080/v1/auth/login`, com o cabeçalho CSRF. Use Body → x-www-form-urlencoded:

| Campo | Valor de exemplo |
| --- | --- |
| `email` | `ana@example.com` |
| `senha` | `minha-senha-forte-123` |

O Content-Type é `application/x-www-form-urlencoded`. O login não recebe JSON. Sucesso retorna `204`, sem corpo, e autentica a sessão. O identificador de sessão muda após o login; preserve o cookie atualizado. Obtenha um novo CSRF em `GET /v1/auth/csrf` após autenticar.

### 4. Sair

Envie `POST http://localhost:8080/v1/auth/logout` com o cookie e o novo CSRF. Retorna `204` e invalida a sessão. Para entrar novamente, obtenha outro token.

## Segurança e erros

A autenticação usa sessão no servidor; não há JWT nem HTTP Basic. A sessão expira após 30 minutos de inatividade. O cookie é HttpOnly e SameSite=Lax. Para acesso por HTTPS, configure `SESSION_COOKIE_SECURE=true`.

A proteção CSRF também é exigida no cadastro e no login, seguindo o [fluxo documentado pelo Spring Security](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html).

| Status | Significado |
| --- | --- |
| 400 | Campos inválidos ou senha acima do limite em bytes |
| 401 | Credenciais incorretas ou tentativa de acesso sem autenticação |
| 403 | CSRF ausente/inválido ou rota não permitida |
| 409 | E-mail já cadastrado ou conflito de integridade |

Conflitos retornam JSON com `message` e `status`. Respostas de segurança `401` e `403` não têm corpo JSON.

Registros antigos de aluno sem e-mail e hash de senha continuam sem acesso. O cadastro cria uma nova conta; não vincula registros antigos automaticamente.

## Testes

```powershell
./mvnw.cmd clean test
```

Os testes não precisam de Docker ou credenciais PostgreSQL. Cobrem inicialização, validação do cadastro, e-mail duplicado, hash BCrypt, limite UTF-8 da senha, credenciais inválidas, login com normalização do e-mail, troca do identificador da sessão, CSRF, logout e bloqueio das rotas removidas. Os resultados com H2 não comprovam comportamento específico do PostgreSQL.

## Documentação no Notion

[Spring Security: cadastro, login e histórico das alterações](https://app.notion.com/p/4077bfee00c6814782e5f5b13b9e5df6). O acesso depende das permissões da página no Notion.
