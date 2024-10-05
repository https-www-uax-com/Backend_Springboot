package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.SampleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/samples")
public class SampleResource {

    private final SampleService sampleService;

    public SampleResource(final SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all samples")
    public ResponseEntity<List<SampleDTO>> getAllSamples(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(sampleService.findAll(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleDTO> getSample(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        SampleDTO sample = sampleService.get(id);
        return ResponseEntity.ok(sample);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new sample")
    public ResponseEntity<Long> createSample(@RequestBody final SampleDTO sampleDTO, @RequestHeader("Authorization") String token) {
        Long createdId = sampleService.create(sampleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSample(@PathVariable final Long id, @RequestBody final SampleDTO sampleDTO, @RequestHeader("Authorization") String token) {
        sampleService.update(id, sampleDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a sample")
    public ResponseEntity<Void> deleteSample(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

