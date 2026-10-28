package Sistema_Academico.projeto_estudo_jpa_security.dto.aluno;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CadastroAlunoRequestDto {
    @NotBlank
    private String nome;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank @Email @Size(max = 254)
    private String email;

    @NotBlank @Size(min = 12, max = 72)
    private String senha;
}
