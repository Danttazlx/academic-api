package Sistema_Academico.projeto_estudo_jpa_security.repository;

import Sistema_Academico.projeto_estudo_jpa_security.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso,Long> {
}
