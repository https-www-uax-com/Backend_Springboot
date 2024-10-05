package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String labName;

    @NotNull
    @Size(max = 255)
    private String location;

}

