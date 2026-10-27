package Sistema_Academico.projeto_estudo_jpa_security.dto.matricula;

import lombok.Data;
import java.time.LocalDate;
import Sistema_Academico.projeto_estudo_jpa_security.enums.MatriculaStatus;

@Data
public class MatriculaResponseDto {
    private Long id;
    private MatriculaStatus status;
    private LocalDate dataMatricula;
    private Long alunoId;
    private Long cursoId;
}