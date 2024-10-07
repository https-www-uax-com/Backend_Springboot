package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {

    private String mensaje;

    private String token;

    private String role;

    public AuthResponseDTO(String mensaje, String token, String role) {
        this.mensaje = mensaje;
        this.token = token;
        this.role = role;
    }

}

