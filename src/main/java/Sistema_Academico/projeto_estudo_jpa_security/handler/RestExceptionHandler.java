package Sistema_Academico.projeto_estudo_jpa_security.handler;

import Sistema_Academico.projeto_estudo_jpa_security.errorResponse.ErrorResponse;
import Sistema_Academico.projeto_estudo_jpa_security.exception.ConflictException;
import Sistema_Academico.projeto_estudo_jpa_security.exception.InternalSeverError;
import Sistema_Academico.projeto_estudo_jpa_security.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException (NotFoundException exception){

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(exception.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());

        return errorResponse;

    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConflictException (ConflictException exception){

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(exception.getMessage());
        errorResponse.setStatus(HttpStatus.CONFLICT.value());

        return errorResponse;

    }

    @ExceptionHandler(InternalSeverError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalSeverError (InternalSeverError exception){

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(exception.getMessage());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return errorResponse;

    }

}
