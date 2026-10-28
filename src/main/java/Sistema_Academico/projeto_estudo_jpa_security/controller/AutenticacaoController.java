package Sistema_Academico.projeto_estudo_jpa_security.controller;

import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.CadastroAlunoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.service.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AutenticacaoController {
    private final AutenticacaoService autenticacao;

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponseDto cadastrar(@Valid @RequestBody CadastroAlunoRequestDto request) {
        return autenticacao.cadastrar(request);
    }
}
