package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.BiologicalData;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.BiologicalDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/biological-datas")
public class BiologicalDataController {

    private final BiologicalDataService biologicalDataService;

    public BiologicalDataController(BiologicalDataService biologicalDataService) {
        this.biologicalDataService = biologicalDataService;
    }

    // Obtener todos los datos biológicos
    @GetMapping
    public ResponseEntity<List<BiologicalDataDTO>> getAllBiologicalData() {
        List<BiologicalDataDTO> biologicalDataList = biologicalDataService.findAll();
        return ResponseEntity.ok(biologicalDataList);
    }

    // Obtener un dato biológico específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<BiologicalDataDTO> getBiologicalDataById(@PathVariable Long id) {
        BiologicalDataDTO biologicalData = biologicalDataService.get(id);
        return ResponseEntity.ok(biologicalData);
    }

    // Crear un nuevo dato biológico
    @PostMapping
    public ResponseEntity<Long> createBiologicalData(@RequestBody BiologicalDataDTO biologicalDataDTO) {
        Long id = biologicalDataService.create(biologicalDataDTO);
        return ResponseEntity.ok(id);
    }

    // Actualizar un dato biológico existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBiologicalData(@PathVariable Long id, @RequestBody BiologicalDataDTO biologicalDataDTO) {
        biologicalDataService.update(id, biologicalDataDTO);
        return ResponseEntity.noContent().build();
    }

    // Eliminar un dato biológico por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBiologicalData(@PathVariable Long id) {
        biologicalDataService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Procesar los datos biológicos de una muestra de forma asincrónica
    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processBiologicalData(@PathVariable Long id) {
        BiologicalDataDTO biologicalDataDTO = biologicalDataService.get(id);
        BiologicalData biologicalData = biologicalDataService.mapToEntity(biologicalDataDTO, new BiologicalData());
        biologicalDataService.processBiologicalData(biologicalData);
        return ResponseEntity.ok().build();
    }

    // Obtener todos los datos biológicos de forma asincrónica
    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<BiologicalDataDTO>>> getAllBiologicalDataAsync() {
        return biologicalDataService.findAllAsync()
                .thenApply(ResponseEntity::ok);
    }
}

