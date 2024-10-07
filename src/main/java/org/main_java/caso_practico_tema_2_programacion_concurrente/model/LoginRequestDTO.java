package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotNull
    private String correo;

    @NotNull
    private String contrasena;
}
