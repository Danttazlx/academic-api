package Sistema_Academico.projeto_estudo_jpa_security.repository;

import Sistema_Academico.projeto_estudo_jpa_security.domain.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
    boolean existsByEmail(String email);
}
