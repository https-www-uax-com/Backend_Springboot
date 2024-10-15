package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Researcher;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ResearcherDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.ResearcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/researchers")
public class ResearcherController {

    private final ResearcherService researcherService;

    public ResearcherController(ResearcherService researcherService) {
        this.researcherService = researcherService;
    }

    @GetMapping
    public ResponseEntity<List<ResearcherDTO>> getAllResearchers() {
        return ResponseEntity.ok(researcherService.findAll());
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<ResearcherDTO>>> getAllResearchersAsync() {
        return researcherService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearcherDTO> getResearcherById(@PathVariable Long id) {
        return ResponseEntity.ok(researcherService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createResearcher(@RequestBody ResearcherDTO researcherDTO) {
        return ResponseEntity.ok(researcherService.create(researcherDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResearcher(@PathVariable Long id, @RequestBody ResearcherDTO researcherDTO) {
        researcherService.update(id, researcherDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResearcher(@PathVariable Long id) {
        researcherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processResearcherById(@PathVariable Long id) {
        Researcher researcher = researcherService.getResearcherEntity(id);  // Debes tener un método en ResearcherService para obtener la entidad
        researcherService.processResearcherAsync(researcher); // Llama al método asincrónico con la entidad
        return ResponseEntity.ok().build();
    }
}
