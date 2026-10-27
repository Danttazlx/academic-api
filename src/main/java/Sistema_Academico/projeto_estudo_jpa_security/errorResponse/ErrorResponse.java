package Sistema_Academico.projeto_estudo_jpa_security.errorResponse;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse{

    private String message;
    private Integer status;

}
