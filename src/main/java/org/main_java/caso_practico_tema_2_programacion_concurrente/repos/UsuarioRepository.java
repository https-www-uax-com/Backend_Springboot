package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Encuentra un usuario por su nombre de usuario
    Optional<Usuario> findByNombre(String nombre);

    // Encuentra un usuario por su correo
    Optional<Usuario> findByCorreo(String correo);

    // Encuentra un usuario por su id de credenciales
    Optional<Usuario> findByUsuario_Id(Long credencialesId);
}