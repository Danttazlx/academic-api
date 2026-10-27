package Sistema_Academico.projeto_estudo_jpa_security.service;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.domain.*;
import Sistema_Academico.projeto_estudo_jpa_security.dto.matricula.MatriculaRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.enums.MatriculaStatus;
import Sistema_Academico.projeto_estudo_jpa_security.repository.*;
import org.junit.jupiter.api.Test;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrudServicesTest {
    private final AlunoRepository alunos = mock(AlunoRepository.class);
    private final CursoRepository cursos = mock(CursoRepository.class);
    private final MatriculaRepository matriculas = mock(MatriculaRepository.class);
    private final MatriculaService service = new MatriculaService(matriculas, alunos, cursos);

    private MatriculaRequestDto request() {
        var request = new MatriculaRequestDto();
        request.setAlunoId(1L);
        request.setCursoId(2L);
        request.setStatus(MatriculaStatus.ATIVA);
        request.setDataMatricula(LocalDate.of(2026, 10, 25));
        return request;
    }

    @Test
    void createResolvesReferencesAndReturnsIds() throws Exception {
        var aluno = new Aluno();
        aluno.setId(1L);
        var curso = new Curso();
        curso.setId(2L);
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursos.findById(2L)).thenReturn(Optional.of(curso));
        when(matriculas.save(any(Matricula.class))).thenAnswer(invocation -> {
            Matricula entity = invocation.getArgument(0);
            entity.setId(3L);
            return entity;
        });
        var result = service.create(request());
        assertEquals(3L, result.getId());
        assertEquals(1L, result.getAlunoId());
        assertEquals(2L, result.getCursoId());
        assertEquals(MatriculaStatus.ATIVA, result.getStatus());
    }

    @Test
    void missingAlunoDoesNotSaveMatricula() {
        when(alunos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.create(request()));
        verify(matriculas, never()).save(any());
    }

    @Test
    void missingMatriculaDoesNotDelete() {
        when(matriculas.findById(8L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(8L));
        verify(matriculas, never()).delete(any());
    }

    @Test
    void cursoWithMatriculaCannotBeDeleted() {
        when(cursos.findById(2L)).thenReturn(Optional.of(new Curso()));
        when(matriculas.existsByCursoId(2L)).thenReturn(true);
        var service = new CursoService(cursos, matriculas);
        assertThrows(ConflictException.class, () -> service.delete(2L));

        verify(cursos, never()).delete(any());
    }
    @Test
    void missingCursoDoesNotSaveMatricula() {
        when(alunos.findById(1L)).thenReturn(Optional.of(new Aluno()));
        when(cursos.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.create(request()));
        verify(matriculas, never()).save(any());
    }

    @Test
    void cursoAlreadyAssignedRejectsNewMatricula() {
        when(alunos.findById(1L)).thenReturn(Optional.of(new Aluno()));
        when(cursos.findById(2L)).thenReturn(Optional.of(new Curso()));
        when(matriculas.existsByCursoIdAndIdNot(2L, -1L)).thenReturn(true);
        assertThrows(ConflictException.class, () -> service.create(request()));
        verify(matriculas, never()).save(any());
    }

    @Test
    void updateKeepsMatriculaIdAndAllowsItsOwnCurso() throws Exception {
        var entity = new Matricula();
        entity.setId(3L);
        var aluno = new Aluno();
        aluno.setId(1L);
        var curso = new Curso();
        curso.setId(2L);
        when(matriculas.findById(3L)).thenReturn(Optional.of(entity));
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursos.findById(2L)).thenReturn(Optional.of(curso));
        when(matriculas.save(entity)).thenReturn(entity);
        var result = service.update(3L, request());
        assertEquals(3L, result.getId());
        assertEquals(2L, result.getCursoId());
        verify(matriculas).existsByCursoIdAndIdNot(2L, 3L);
    }}
