package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.AnalysisResult;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.AnalysisResultDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.AnalysisResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/analysis-result")
public class AnalysisResultController {

    private final AnalysisResultService analysisResultService;

    public AnalysisResultController(AnalysisResultService analysisResultService) {
        this.analysisResultService = analysisResultService;
    }

    @GetMapping
    public ResponseEntity<List<AnalysisResultDTO>> getAllAnalysisResults() {
        return ResponseEntity.ok(analysisResultService.findAll());
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<AnalysisResultDTO>>> getAllAnalysisResultsAsync() {
        return analysisResultService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResultDTO> getAnalysisResultById(@PathVariable Long id) {
        return ResponseEntity.ok(analysisResultService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createAnalysisResult(@RequestBody AnalysisResultDTO analysisResultDTO) {
        return ResponseEntity.ok(analysisResultService.create(analysisResultDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnalysisResult(@PathVariable Long id, @RequestBody AnalysisResultDTO analysisResultDTO) {
        analysisResultService.update(id, analysisResultDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable Long id) {
        analysisResultService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processAnalysisResultsConcurrently() {
        analysisResultService.processAnalysisResultsConcurrently();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processSingleAnalysisResult(@PathVariable Long id) {
        // Mapeamos el DTO a entidad antes de procesar
        AnalysisResultDTO resultDTO = analysisResultService.get(id);
        analysisResultService.processSingleAnalysisResult(analysisResultService.mapToEntity(resultDTO, new AnalysisResult()));
        return ResponseEntity.ok().build();
    }

}
