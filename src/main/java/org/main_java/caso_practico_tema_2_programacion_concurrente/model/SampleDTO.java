package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String sampleCode;

    @NotNull
    private Long experimentId;  // ID del experimento al que pertenece la muestra

    @NotNull
    private BiologicalDataDTO biologicalData;  // Datos biológicos relacionados con la muestra

}

