package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.LabDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.LabService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
public class LabResource {

    private final LabService labService;

    public LabResource(final LabService labService) {
        this.labService = labService;
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all labs")
    public ResponseEntity<List<LabDTO>> getAllLabs() {
        return ResponseEntity.ok(labService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabDTO> getLab(@PathVariable final Long id) {
        return ResponseEntity.ok(labService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new lab")
    public ResponseEntity<Long> createLab(@RequestBody final LabDTO labDTO) {
        final Long createdId = labService.create(labDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLab(@PathVariable final Long id,
                                          @RequestBody final LabDTO labDTO) {
        labService.update(id, labDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a lab")
    public ResponseEntity<Void> deleteLab(@PathVariable final Long id) {
        labService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


