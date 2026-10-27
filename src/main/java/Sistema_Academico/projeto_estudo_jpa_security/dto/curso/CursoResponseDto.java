package Sistema_Academico.projeto_estudo_jpa_security.dto.curso;

import lombok.Data;


@Data
public class CursoResponseDto {
    private Long id;
    private String nome;
    private Integer cargaHoraria;
    private String descricao;
}