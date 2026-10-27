package Sistema_Academico.projeto_estudo_jpa_security.service;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.domain.Matricula;
import Sistema_Academico.projeto_estudo_jpa_security.dto.matricula.MatriculaRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.matricula.MatriculaResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatriculaService {
    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    @Transactional
    public MatriculaResponseDto create(MatriculaRequestDto request) throws NotFoundException {
        Matricula entity = new Matricula();
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    public List<MatriculaResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public MatriculaResponseDto getById(Long id) throws NotFoundException {
        return toResponse(find(id));
    }

    @Transactional
    public MatriculaResponseDto update(Long id, MatriculaRequestDto request) throws NotFoundException {
        Matricula entity = find(id);
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {
        Matricula entity = find(id);

        repository.delete(entity);
    }

    private Matricula find(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Matricula nao encontrada"));
    }

    private void apply(Matricula entity, MatriculaRequestDto request) throws NotFoundException {
        var aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new NotFoundException("Aluno nao encontrado"));
        var curso = cursoRepository.findById(request.getCursoId())
                .orElseThrow(() -> new NotFoundException("Curso nao encontrado"));
        if (repository.existsByCursoIdAndIdNot(request.getCursoId(), entity.getId() == null ? -1L : entity.getId())) {
            throw new ConflictException("Curso ja possui matricula");
        }
        entity.setStatus(request.getStatus());
        entity.setDataMatricula(request.getDataMatricula());
        entity.setAluno(aluno);
        entity.setCurso(curso);
    }

    private MatriculaResponseDto toResponse(Matricula entity) {
        MatriculaResponseDto dto = new MatriculaResponseDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setDataMatricula(entity.getDataMatricula());
        dto.setAlunoId(entity.getAluno().getId());
        dto.setCursoId(entity.getCurso().getId());
        return dto;
    }
}