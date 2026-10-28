package Sistema_Academico.projeto_estudo_jpa_security;

import Sistema_Academico.projeto_estudo_jpa_security.repository.AlunoRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationTests {
    @Autowired WebApplicationContext context;
    @Autowired @Qualifier("springSecurityFilterChain") Filter filter;
    @Autowired AlunoRepository alunos;
    MockMvc mvc;
    MockHttpSession session;
    String csrf;

    @BeforeEach
    void setup() throws Exception {
        alunos.deleteAllInBatch();
        mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(filter).build();
        refreshCsrf();
    }

    void refreshCsrf() throws Exception {
        var request = get("/v1/auth/csrf");
        if (session != null && !session.isInvalid()) request.session(session);
        var result = mvc.perform(request).andExpect(status().isOk()).andReturn();
        session = (MockHttpSession) result.getRequest().getSession();
        csrf = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    ResultActions register(String email, String senha) throws Exception {
        return mvc.perform(post("/v1/auth/cadastro").session(session).header("X-CSRF-TOKEN", csrf)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Ana\",\"dataNascimento\":\"2000-01-01\",\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}"));
    }

    void login() throws Exception {
        var result = mvc.perform(post("/v1/auth/login").session(session).header("X-CSRF-TOKEN", csrf)
            .param("email", "ANA@example.com").param("senha", "senha-segura-123"))
            .andExpect(status().isNoContent()).andReturn();
        session = (MockHttpSession) result.getRequest().getSession();
    }

    @Test void registrationHashesPasswordAndNeverReturnsIt() throws Exception {
        register("ANA@example.com", "senha-segura-123").andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.senha").doesNotExist())
            .andExpect(jsonPath("$.senhaHash").doesNotExist());
        var aluno = alunos.findByEmail("ana@example.com").orElseThrow();
        assertTrue(new BCryptPasswordEncoder().matches("senha-segura-123", aluno.getSenhaHash()));
    }

    @Test void rejectsDuplicateAndWeakPasswords() throws Exception {
        register("ana@example.com", "curta").andExpect(status().isBadRequest());
        register("ana@example.com", "senha-segura-123").andExpect(status().isCreated());
        register("ANA@example.com", "senha-segura-123").andExpect(status().isConflict());
        assertEquals(1, alunos.count());
    }

    @Test void rejectsInvalidEmailAndMultibytePasswordAboveBcryptLimit() throws Exception {
        register("invalido", "senha-segura-123").andExpect(status().isBadRequest());
        register("ana@example.com", String.valueOf((char) 233).repeat(40)).andExpect(status().isBadRequest());
        assertEquals(0, alunos.count());
    }

    @Test void csrfRequiredForRegistrationAndLogin() throws Exception {
        mvc.perform(post("/v1/auth/cadastro").contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isForbidden());
        mvc.perform(post("/v1/auth/login").param("email", "x").param("senha", "x"))
            .andExpect(status().isForbidden());
    }

    @Test void wrongPasswordAndUnknownAccountRejected() throws Exception {
        register("ana@example.com", "senha-segura-123").andExpect(status().isCreated());
        for (String email : new String[] {"ana@example.com", "nao-existe@example.com"}) {
            mvc.perform(post("/v1/auth/login").session(session).header("X-CSRF-TOKEN", csrf)
                .param("email", email).param("senha", "errada"))
                .andExpect(status().isUnauthorized());
        }
    }

    @Test void loginAuthenticatesStudentAndRotatesSessionId() throws Exception {
        register("ana@example.com", "senha-segura-123").andExpect(status().isCreated());
        String oldId = session.getId();
        login();
        assertNotEquals(oldId, session.getId());
        var security = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        assertTrue(security.getAuthentication().isAuthenticated());
        assertEquals("ana@example.com", security.getAuthentication().getName());
    }

    @Test void logoutRequiresCsrfAndInvalidatesSession() throws Exception {
        register("ana@example.com", "senha-segura-123").andExpect(status().isCreated());
        login();
        mvc.perform(post("/v1/auth/logout").session(session)).andExpect(status().isForbidden());
        refreshCsrf();
        mvc.perform(post("/v1/auth/logout").session(session).header("X-CSRF-TOKEN", csrf))
            .andExpect(status().isNoContent());
        assertTrue(session.isInvalid());
    }

    @Test void removedEndpointsRemainInaccessibleAfterLogin() throws Exception {
        register("ana@example.com", "senha-segura-123").andExpect(status().isCreated());
        login();
        for (String path : new String[] {"/v1/school/alunos", "/v1/school/cursos",
                "/v1/school/matriculas", "/v1/auth/me", "/v1/auth/minhas-matriculas"}) {
            mvc.perform(get(path).session(session)).andExpect(status().isForbidden());
        }
    }
}
