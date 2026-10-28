package Sistema_Academico.projeto_estudo_jpa_security.service;

import Sistema_Academico.projeto_estudo_jpa_security.domain.Aluno;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.CadastroAlunoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;
import Sistema_Academico.projeto_estudo_jpa_security.repository.AlunoRepository;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {
    private final AlunoRepository alunos;
    private final PasswordEncoder encoder;

    @Transactional
    public AlunoResponseDto cadastrar(CadastroAlunoRequestDto request) {
        String email = request.getEmail().strip().toLowerCase(Locale.ROOT);
        if (alunos.existsByEmail(email)) {
            throw new ConflictException("Cadastro indisponivel para este email");
        }
        if (request.getSenha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Senha deve ter no maximo 72 bytes UTF-8");
        }

        var aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setEmail(email);
        aluno.setSenhaHash(encoder.encode(request.getSenha()));
        aluno = alunos.saveAndFlush(aluno);

        var dto = new AlunoResponseDto();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setDataNascimento(aluno.getDataNascimento());
        return dto;
    }
}
