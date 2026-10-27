package Sistema_Academico.projeto_estudo_jpa_security.service;

import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import Sistema_Academico.projeto_estudo_jpa_security.domain.Aluno;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoRequestDto;
import Sistema_Academico.projeto_estudo_jpa_security.dto.aluno.AlunoResponseDto;
import Sistema_Academico.projeto_estudo_jpa_security.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    // cria aluno
    @Transactional
    public AlunoResponseDto createStudent(AlunoRequestDto alunoDto) {

        Aluno aluno = new Aluno();

        aluno.setNome(alunoDto.getNome());
        aluno.setDataNascimento(alunoDto.getDataNascimento());

        var response = alunoRepository.save(aluno);

        AlunoResponseDto respondeEntity = new AlunoResponseDto();

        respondeEntity.setNome(response.getNome());
        respondeEntity.setDataNascimento(response.getDataNascimento());
        respondeEntity.setId(response.getId());

        return respondeEntity;
    }


    public Page<Aluno> getAllStudent(Pageable pageable) {
      return alunoRepository.findAll(pageable);

    }



    @Transactional
    public void deleteStudent(Long id) throws NotFoundException {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student is Not Found"));
        alunoRepository.delete(aluno);
    }


    @Transactional
    public AlunoResponseDto alterStudent(Long id, AlunoRequestDto student) throws NotFoundException {

        Aluno studentDb = alunoRepository.findByIds(id)
                .orElseThrow(() -> new NotFoundException("Student is Not Found"));

        studentDb.setNome(student.getNome());
        studentDb.setDataNascimento(student.getDataNascimento());

        var response = alunoRepository.save(studentDb);

        AlunoResponseDto alunoResponseDto = new AlunoResponseDto();

        alunoResponseDto.setId(response.getId());
        alunoResponseDto.setNome(response.getNome());
        alunoResponseDto.setDataNascimento(response.getDataNascimento());

        return alunoResponseDto;

    }


    public AlunoResponseDto getStudent(Long id) throws NotFoundException {

        Aluno responseDb = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student is Not Found "));

        AlunoResponseDto alunoResponseDto = new AlunoResponseDto();

        alunoResponseDto.setId(responseDb.getId());
        alunoResponseDto.setNome(responseDb.getNome());
        alunoResponseDto.setDataNascimento(responseDb.getDataNascimento());

        return  alunoResponseDto;

    }
}