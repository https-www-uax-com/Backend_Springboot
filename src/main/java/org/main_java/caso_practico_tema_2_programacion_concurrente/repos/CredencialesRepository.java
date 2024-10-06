package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {

    // Encuentra credenciales por el nombre de usuario
    Optional<Credenciales> findByUsername(String username);
}