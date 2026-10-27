package Sistema_Academico.projeto_estudo_jpa_security.dto.curso;

import lombok.Data;
import jakarta.validation.constraints.*;


@Data
public class CursoRequestDto {
    @NotBlank
    private String nome;
    @NotNull @Positive
    private Integer cargaHoraria;
    @Size(max = 300)
    private String descricao;
}