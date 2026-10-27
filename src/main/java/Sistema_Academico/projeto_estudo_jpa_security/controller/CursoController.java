package Sistema_Academico.projeto_estudo_jpa_security.controller;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.dto.curso.CursoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.curso.CursoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("v1/school/cursos")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponseDto create(@Valid @RequestBody CursoRequestDto request) {
        return service.create(request);
    }

    @GetMapping
    public List<CursoResponseDto> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public CursoResponseDto getById(@PathVariable Long id) throws NotFoundException {
        return service.getById(id);
    }

    @PutMapping("{id}")
    public CursoResponseDto update(@PathVariable Long id, @Valid @RequestBody CursoRequestDto request) throws NotFoundException {
        return service.update(id, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        service.delete(id);
    }
}