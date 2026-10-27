package Sistema_Academico.projeto_estudo_jpa_security.exception;

import lombok.Data;

@Data
public class NotFoundException extends Exception{

    public NotFoundException(String message) {
       super(message);
    }
}
