package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BiologicalDataDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String sampleType;  // Tipo de muestra [genetica, bioquimica, fisica]

    @NotNull
    private String data;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private String analysisResult;

}

