package Sistema_Academico.projeto_estudo_jpa_security.repository;

import Sistema_Academico.projeto_estudo_jpa_security.domain.Aluno;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("select a from Aluno a where a.id = :id")
    Optional<Aluno> findByIds(@NonNull @Param("id") Long id);

}
