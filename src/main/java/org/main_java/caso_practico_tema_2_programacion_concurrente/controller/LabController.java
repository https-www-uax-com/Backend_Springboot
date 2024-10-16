package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Lab;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.LabDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.LabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/lab")
public class LabController {

    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public ResponseEntity<List<LabDTO>> getAllLabs() {
        return ResponseEntity.ok(labService.findAll());
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<LabDTO>>> getAllLabsAsync() {
        return labService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabDTO> getLabById(@PathVariable Long id) {
        return ResponseEntity.ok(labService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createLab(@RequestBody LabDTO labDTO) {
        return ResponseEntity.ok(labService.create(labDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLab(@PathVariable Long id, @RequestBody LabDTO labDTO) {
        labService.update(id, labDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        labService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processLabById(@PathVariable Long id) {
        Lab lab = labService.getLabEntity(id);  // Debes tener un método en LabService para obtener la entidad Lab
        labService.processLabAsync(lab); // Llama al método asincrónico con la entidad
        return ResponseEntity.ok().build();
    }

}
