package Sistema_Academico.projeto_estudo_jpa_security.repository;

import Sistema_Academico.projeto_estudo_jpa_security.domain.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula,Long> {
    boolean existsByCursoId(Long cursoId);

    boolean existsByCursoIdAndIdNot(Long cursoId, Long id);
}
