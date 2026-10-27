package Sistema_Academico.projeto_estudo_jpa_security.service;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.domain.Curso;
import Sistema_Academico.projeto_estudo_jpa_security.dto.curso.CursoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.curso.CursoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CursoService {
    private final CursoRepository repository;
    private final MatriculaRepository matriculaRepository;

    @Transactional
    public CursoResponseDto create(CursoRequestDto request) {
        Curso entity = new Curso();
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    public List<CursoResponseDto> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public CursoResponseDto getById(Long id) throws NotFoundException {
        return toResponse(find(id));
    }

    @Transactional
    public CursoResponseDto update(Long id, CursoRequestDto request) throws NotFoundException {
        Curso entity = find(id);
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {
        Curso entity = find(id);
        if (matriculaRepository.existsByCursoId(id)) {
            throw new ConflictException("Curso possui matricula vinculada");
        }
        repository.delete(entity);
    }

    private Curso find(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Curso nao encontrado"));
    }

    private void apply(Curso entity, CursoRequestDto request) {
        entity.setNome(request.getNome());
        entity.setCargaHoraria(request.getCargaHoraria());
        entity.setDescricao(request.getDescricao());
    }

    private CursoResponseDto toResponse(Curso entity) {
        CursoResponseDto dto = new CursoResponseDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCargaHoraria(entity.getCargaHoraria());
        dto.setDescricao(entity.getDescricao());
        return dto;
    }
}