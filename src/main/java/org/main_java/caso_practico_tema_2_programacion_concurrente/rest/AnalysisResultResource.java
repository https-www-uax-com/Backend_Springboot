package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.AnalysisResultDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.AnalysisResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/analysis-results")
public class AnalysisResultResource {

    private final AnalysisResultService analysisResultService;

    public AnalysisResultResource(final AnalysisResultService analysisResultService) {
        this.analysisResultService = analysisResultService;
    }

    @PostMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all analysis results")
    public ResponseEntity<List<AnalysisResultDTO>> getAllAnalysisResults(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(analysisResultService.findAll(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResultDTO> getAnalysisResult(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        AnalysisResultDTO analysisResult = analysisResultService.get(id);
        return ResponseEntity.ok(analysisResult);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new analysis result")
    public ResponseEntity<Long> createAnalysisResult(@RequestBody final AnalysisResultDTO analysisResultDTO, @RequestHeader("Authorization") String token) {
        Long createdId = analysisResultService.create(analysisResultDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnalysisResult(@PathVariable final Long id, @RequestBody final AnalysisResultDTO analysisResultDTO, @RequestHeader("Authorization") String token) {
        analysisResultService.update(id, analysisResultDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete an analysis result")
    public ResponseEntity<Void> deleteAnalysisResult(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        analysisResultService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
