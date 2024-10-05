package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ResearcherDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.ResearcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/researchers")
public class ResearcherResource {

    private final ResearcherService researcherService;

    public ResearcherResource(final ResearcherService researcherService) {
        this.researcherService = researcherService;
    }

    @PostMapping("/list")
    @ApiResponse(responseCode = "200", description = "Get all researchers")
    public ResponseEntity<List<ResearcherDTO>> getAllResearchers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(researcherService.findAll(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearcherDTO> getResearcher(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        ResearcherDTO researcher = researcherService.get(id);
        return ResponseEntity.ok(researcher);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new researcher")
    public ResponseEntity<Long> createResearcher(@RequestBody final ResearcherDTO researcherDTO, @RequestHeader("Authorization") String token) {
        Long createdId = researcherService.create(researcherDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResearcher(@PathVariable final Long id, @RequestBody final ResearcherDTO researcherDTO, @RequestHeader("Authorization") String token) {
        researcherService.update(id, researcherDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a researcher")
    public ResponseEntity<Void> deleteResearcher(@PathVariable final Long id, @RequestHeader("Authorization") String token) {
        researcherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
