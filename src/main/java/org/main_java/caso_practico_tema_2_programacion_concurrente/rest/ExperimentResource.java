package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ExperimentDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.ExperimentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentResource {

    private final ExperimentService experimentService;

    public ExperimentResource(final ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all experiments")
    public ResponseEntity<List<ExperimentDTO>> getAllExperiments() {
        return ResponseEntity.ok(experimentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentDTO> getExperiment(@PathVariable final Long id) {
        return ResponseEntity.ok(experimentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new experiment")
    public ResponseEntity<Long> createExperiment(@RequestBody final ExperimentDTO experimentDTO) {
        final Long createdId = experimentService.create(experimentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExperiment(@PathVariable final Long id,
                                                 @RequestBody final ExperimentDTO experimentDTO) {
        experimentService.update(id, experimentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete an experiment")
    public ResponseEntity<Void> deleteExperiment(@PathVariable final Long id) {
        experimentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


