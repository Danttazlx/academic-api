package Sistema_Academico.projeto_estudo_jpa_security.dto.matricula;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import Sistema_Academico.projeto_estudo_jpa_security.enums.MatriculaStatus;

@Data
public class MatriculaRequestDto {
    @NotNull
    private MatriculaStatus status;
    @NotNull
    private LocalDate dataMatricula;
    @NotNull @Positive
    private Long alunoId;
    @NotNull @Positive
    private Long cursoId;
}