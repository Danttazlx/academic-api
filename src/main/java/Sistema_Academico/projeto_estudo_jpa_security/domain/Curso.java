package Sistema_Academico.projeto_estudo_jpa_security.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String nome;

@Column(name = "carga_horaria")
private Integer cargaHoraria;

@Column(length = 300)
private String descricao;

@OneToOne(mappedBy = "curso")
private Matricula matricula;


}
