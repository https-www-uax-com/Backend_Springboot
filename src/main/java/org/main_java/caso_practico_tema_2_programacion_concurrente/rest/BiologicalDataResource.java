package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.BiologicalDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/biological-data")
public class BiologicalDataResource {

    private final BiologicalDataService biologicalDataService;

    public BiologicalDataResource(final BiologicalDataService biologicalDataService) {
        this.biologicalDataService = biologicalDataService;
    }

    @PostMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all biological data")
    public ResponseEntity<List<BiologicalDataDTO>> getAllBiologicalData(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(biologicalDataService.findAll(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiologicalDataDTO> getBiologicalData(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        BiologicalDataDTO biologicalData = biologicalDataService.get(id);
        return ResponseEntity.ok(biologicalData);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new biological data record")
    public ResponseEntity<Long> createBiologicalData(@RequestBody final BiologicalDataDTO biologicalDataDTO, @RequestHeader("Authorization") String token) {
        Long createdId = biologicalDataService.create(biologicalDataDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBiologicalData(@PathVariable final Long id, @RequestBody final BiologicalDataDTO biologicalDataDTO, @RequestHeader("Authorization") String token) {
        biologicalDataService.update(id, biologicalDataDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a biological data record")
    public ResponseEntity<Void> deleteBiologicalData(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        biologicalDataService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
