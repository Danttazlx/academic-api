package Sistema_Academico.projeto_estudo_jpa_security.controller;


import Sistema_Academico.projeto_estudo_jpa_security.domain.Aluno;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"v1/school/alunos"})
@RequiredArgsConstructor
public class AlunoController {

  private final AlunoService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AlunoResponseDto creatStudent(@Valid @RequestBody AlunoRequestDto student) {
    return service.createStudent(student);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Aluno> getAllStudent(Pageable pageable) {
    return service.getAllStudent(pageable);
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public AlunoResponseDto getStudent(@PathVariable Long id) throws NotFoundException {
    return service.getStudent(id);
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("{id}")
  public void deleteStudent(@PathVariable Long id) throws NotFoundException {
    service.deleteStudent(id);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{id}")
  public AlunoResponseDto alterStudent(@PathVariable Long id,
                                       @Valid @RequestBody AlunoRequestDto student) throws NotFoundException {
    return service.alterStudent(id, student);
  }




}