package Sistema_Academico.projeto_estudo_jpa_security.dto.aluno;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoResponseDto {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;


}
