package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;


@Getter
@Setter
public class RolDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    // private Set<UsuarioDTO> rolId;
    // Se consideren innecesarios pero se dejan por si se requieren en un futuro
}