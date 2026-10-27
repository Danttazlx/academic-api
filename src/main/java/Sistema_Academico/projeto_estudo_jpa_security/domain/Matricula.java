package Sistema_Academico.projeto_estudo_jpa_security.domain;


import Sistema_Academico.projeto_estudo_jpa_security.enums.MatriculaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "matricula")
public class Matricula {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MatriculaStatus status;

    @Column(name = "data_matricula")
    private LocalDate dataMatricula;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @OneToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;


}
