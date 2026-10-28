package Sistema_Academico.projeto_estudo_jpa_security.config;

import Sistema_Academico.projeto_estudo_jpa_security.repository.AlunoRepository;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityCofiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/v1/auth/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/cadastro", "/v1/auth/login").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().denyAll())
            .formLogin(form -> form.loginProcessingUrl("/v1/auth/login")
                .usernameParameter("email")
                .passwordParameter("senha")
                .successHandler((req, res, auth) -> res.setStatus(204))
                .failureHandler((req, res, ex) -> res.setStatus(401)))
            .logout(logout -> logout.logoutUrl("/v1/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(204)))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, error) -> res.setStatus(401))
                .accessDeniedHandler((req, res, error) -> res.setStatus(403)))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService(AlunoRepository alunos) {
        return login -> {
            String email = login.strip().toLowerCase(Locale.ROOT);
            var aluno = alunos.findByEmail(email)
                .filter(a -> a.getSenhaHash() != null)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais invalidas"));
            return User.withUsername(aluno.getEmail())
                .password(aluno.getSenhaHash())
                .roles("ALUNO")
                .build();
        };
    }
}
