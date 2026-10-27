package Sistema_Academico.projeto_estudo_jpa_security.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}