package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ExperimentDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.ExperimentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/experiment")
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping
    public ResponseEntity<List<ExperimentDTO>> getAllExperiments() {
        return ResponseEntity.ok(experimentService.findAll());
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<ExperimentDTO>>> getAllExperimentsAsync() {
        return experimentService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentDTO> getExperimentById(@PathVariable Long id) {
        return ResponseEntity.ok(experimentService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createExperiment(@RequestBody Experiment experiment) {
        return ResponseEntity.ok(experimentService.create(experimentService.mapToDTO(experiment)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExperiment(@PathVariable Long id, @RequestBody ExperimentDTO experimentDTO) {
        try {
            experimentService.update(id, experimentDTO);
            return ResponseEntity.ok("Experiment updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperiment(@PathVariable Long id) {
        experimentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processExperimentsConcurrently() {
        experimentService.processExperimentsConcurrently();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processSingleExperiment(@PathVariable Long id) {
        experimentService.processSingleExperiment(experimentService.get(id));
        return ResponseEntity.ok().build();
    }
}

