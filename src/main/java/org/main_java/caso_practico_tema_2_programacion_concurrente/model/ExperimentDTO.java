package org.main_java.caso_practico_tema_2_programacion_concurrente.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExperimentDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String experimentName;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    private List<SampleDTO> samples;  // Lista de muestras asociadas al experimento

    @NotNull
    private String status;
}

