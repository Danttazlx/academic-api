package Sistema_Academico.projeto_estudo_jpa_security.handler;

import Sistema_Academico.projeto_estudo_jpa_security.errorResponse.ErrorResponse;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConflict() {
        var response = new ErrorResponse();
        response.setMessage("Cadastro indisponivel para este email");
        response.setStatus(409);
        return response;
    }
}
