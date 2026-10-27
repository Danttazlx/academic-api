package Sistema_Academico.projeto_estudo_jpa_security.dto.aluno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoRequestDto{
    @NotBlank
    String nome;
    @NotNull
    LocalDate dataNascimento;
}
