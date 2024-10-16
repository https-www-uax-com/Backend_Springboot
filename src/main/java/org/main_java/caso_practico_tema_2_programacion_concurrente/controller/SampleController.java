package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.SampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping
    public ResponseEntity<List<SampleDTO>> getAllSamples() {
        return ResponseEntity.ok(sampleService.findAll());
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<SampleDTO>>> getAllSamplesAsync() {
        return sampleService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleDTO> getSampleById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createSample(@RequestBody SampleDTO sampleDTO) {
        return ResponseEntity.ok(sampleService.create(sampleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSample(@PathVariable Long id, @RequestBody SampleDTO sampleDTO) {
        sampleService.update(id, sampleDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable Long id) {
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processSamplesConcurrently() {
        sampleService.processSamplesConcurrently();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<Void> processSingleSample(@PathVariable Long id) {
        sampleService.processSingleSample(id);
        return ResponseEntity.ok().build();
    }
}
