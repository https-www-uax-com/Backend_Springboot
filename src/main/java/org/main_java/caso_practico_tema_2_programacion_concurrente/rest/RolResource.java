package org.main_java.caso_practico_tema_2_programacion_concurrente.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.RolDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RolResource {

    private final RolService rolService;

    public RolResource(final RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Get all roles")
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRol(@PathVariable final Long id) {
        RolDTO rol = rolService.get(id);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create a new role")
    public ResponseEntity<Long> createRol(@RequestBody final RolDTO rolDTO) {
        Long createdId = rolService.create(rolDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRol(@PathVariable final Long id,
                                          @RequestBody final RolDTO rolDTO) {
        rolService.update(id, rolDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Delete a role")
    public ResponseEntity<Void> deleteRol(@PathVariable final Long id) {
        rolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

