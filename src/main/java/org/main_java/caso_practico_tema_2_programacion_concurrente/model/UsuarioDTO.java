package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;


@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    private String apellido1;

    @NotNull
    @Size(max = 255)
    private String apellido2;

    @NotNull
    @Size(max = 255)
    private String correo;

    @NotNull
    private Integer telefono;

    @Size(max = 255)
    private String direccion;

    private RolDTO usuarios;

    private CredencialesDTO usuario;

    private OffsetDateTime dateCreated;
}