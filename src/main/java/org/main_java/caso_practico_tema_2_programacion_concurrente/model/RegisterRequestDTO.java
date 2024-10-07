package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotNull
    private String nombre;

    @NotNull
    private String apellido1;

    @NotNull
    private String apellido2;

    @NotNull
    @Size(max = 255)
    private String correo;

    @NotNull
    @Size(min = 6, max = 255)
    private String contrasena;

    @NotNull
    private Integer telefono;

    private String direccion;

    @NotNull
    private String role;
}

