package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.AnalysisResultDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.AnalysisResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis-results")
public class AnalysisResultResource {

    private final AnalysisResultService analysisResultService;

    public AnalysisResultResource(final AnalysisResultService analysisResultService) {
        this.analysisResultService = analysisResultService;
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all analysis results")
    public ResponseEntity<List<AnalysisResultDTO>> getAllAnalysisResults() {
        return ResponseEntity.ok(analysisResultService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResultDTO> getAnalysisResult(@PathVariable final Long id) {
        return ResponseEntity.ok(analysisResultService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new analysis result")
    public ResponseEntity<Long> createAnalysisResult(@RequestBody final AnalysisResultDTO analysisResultDTO) {
        final Long createdId = analysisResultService.create(analysisResultDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnalysisResult(@PathVariable final Long id,
                                                     @RequestBody final AnalysisResultDTO analysisResultDTO) {
        analysisResultService.update(id, analysisResultDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete an analysis result")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable final Long id) {
        analysisResultService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
