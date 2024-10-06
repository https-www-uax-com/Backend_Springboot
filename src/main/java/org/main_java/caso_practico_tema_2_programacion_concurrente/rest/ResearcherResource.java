package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ResearcherDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.ResearcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/researchers")
public class ResearcherResource {

    private final ResearcherService researcherService;

    public ResearcherResource(final ResearcherService researcherService) {
        this.researcherService = researcherService;
    }

    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all researchers")
    public ResponseEntity<List<ResearcherDTO>> getAllResearchers() {
        return ResponseEntity.ok(researcherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearcherDTO> getResearcher(@PathVariable final Long id) {
        return ResponseEntity.ok(researcherService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new researcher")
    public ResponseEntity<Long> createResearcher(@RequestBody final ResearcherDTO researcherDTO) {
        final Long createdId = researcherService.create(researcherDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResearcher(@PathVariable final Long id,
                                                 @RequestBody final ResearcherDTO researcherDTO) {
        researcherService.update(id, researcherDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a researcher")
    public ResponseEntity<Void> deleteResearcher(@PathVariable final Long id) {
        researcherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

