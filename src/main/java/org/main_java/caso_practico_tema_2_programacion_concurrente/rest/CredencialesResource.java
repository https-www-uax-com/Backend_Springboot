package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.CredencialesDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.CredencialesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/credenciales")
public class CredencialesResource {

    private final CredencialesService credencialesService;

    public CredencialesResource(final CredencialesService credencialesService) {
        this.credencialesService = credencialesService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Get all credentials")
    public ResponseEntity<List<CredencialesDTO>> getAllCredenciales() {
        return ResponseEntity.ok(credencialesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredencialesDTO> getCredenciales(@PathVariable final Long id) {
        CredencialesDTO credenciales = credencialesService.get(id);
        return ResponseEntity.ok(credenciales);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create new credentials")
    public ResponseEntity<Long> createCredenciales(@RequestBody final CredencialesDTO credencialesDTO) {
        Long createdId = credencialesService.create(credencialesDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCredenciales(@PathVariable final Long id,
                                                   @RequestBody final CredencialesDTO credencialesDTO) {
        credencialesService.update(id, credencialesDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete credentials")
    public ResponseEntity<Void> deleteCredenciales(@PathVariable final Long id) {
        credencialesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

