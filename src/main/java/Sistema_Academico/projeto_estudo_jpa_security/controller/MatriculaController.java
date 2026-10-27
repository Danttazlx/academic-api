package Sistema_Academico.projeto_estudo_jpa_security.controller;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.dto.matricula.MatriculaRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.matricula.MatriculaResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("v1/school/matriculas")
@RequiredArgsConstructor
public class MatriculaController {
    private final MatriculaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponseDto create(@Valid @RequestBody MatriculaRequestDto request) throws NotFoundException {
        return service.create(request);
    }

    @GetMapping
    public List<MatriculaResponseDto> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public MatriculaResponseDto getById(@PathVariable Long id) throws NotFoundException {
        return service.getById(id);
    }

    @PutMapping("{id}")
    public MatriculaResponseDto update(@PathVariable Long id, @Valid @RequestBody MatriculaRequestDto request) throws NotFoundException {
        return service.update(id, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        service.delete(id);
    }
}