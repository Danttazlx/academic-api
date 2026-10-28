package Sistema_Academico.projeto_estudo_jpa_security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, length = 254)
    private String email;

    @JsonIgnore
    @Column(name = "senha_hash", length = 60)
    private String senhaHash;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
}
