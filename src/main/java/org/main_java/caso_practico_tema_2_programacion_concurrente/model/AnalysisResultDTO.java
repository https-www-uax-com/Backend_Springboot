package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisResultDTO {

    private Long id;

    @NotNull
    private SampleDTO sample;  // Muestra asociada al analisis

    @NotNull
    private String result;     // Resultado del analisis

}

