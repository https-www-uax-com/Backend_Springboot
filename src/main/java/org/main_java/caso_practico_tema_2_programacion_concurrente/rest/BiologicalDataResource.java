package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.BiologicalDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biological-data")
public class BiologicalDataResource {

    private final BiologicalDataService biologicalDataService;

    public BiologicalDataResource(final BiologicalDataService biologicalDataService) {
        this.biologicalDataService = biologicalDataService;
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all biological data")
    public ResponseEntity<List<BiologicalDataDTO>> getAllBiologicalData() {
        return ResponseEntity.ok(biologicalDataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiologicalDataDTO> getBiologicalData(@PathVariable final Long id) {
        return ResponseEntity.ok(biologicalDataService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create new biological data")
    public ResponseEntity<Long> createBiologicalData(@RequestBody final BiologicalDataDTO biologicalDataDTO) {
        final Long createdId = biologicalDataService.create(biologicalDataDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBiologicalData(@PathVariable final Long id,
                                                     @RequestBody final BiologicalDataDTO biologicalDataDTO) {
        biologicalDataService.update(id, biologicalDataDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete biological data")
    public ResponseEntity<Void> deleteBiologicalData(@PathVariable final Long id) {
        biologicalDataService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

