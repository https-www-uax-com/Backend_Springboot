package org.main_java.caso_practico_tema_2_programacion_concurrente;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.RegisterRequestDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.LoginRequestDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@SpringBootApplication
public class CasoPracticoTema2ProgramacionConcurrenteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasoPracticoTema2ProgramacionConcurrenteApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(AuthService authService) {
        return args -> {
            // Registro de usuarios
            registrarNuevoUsuario(
                    authService,
                    "Administrador", "ApellidoA", "ApellidoB", "admin@gmail.com", 123456789,
                    "Calle Principal 123", "a12345_67", "admin"
            );

            registrarNuevoUsuario(
                    authService,
                    "Investigador", "ApellidoAA", "ApellidoBB", "researcher@gmail.com", 987654321,
                    "Calle Secundaria 456", "a12345_67", "researcher"
            );

            registrarNuevoUsuario(
                    authService,
                    "Usuario", "ApellidoCC", "ApellidoDD", "usuario@gmail.com", 555666777,
                    "Avenida Tercera 789", "a12345_67", "user"
            );
        };
    }

    private String registrarNuevoUsuario(AuthService authService, String nombre, String apellido1, String apellido2,
                                         String correo, int telefono, String direccion, String contrasena, String rolNombre) {

        // Crear el objeto RegisterRequest con la información del usuario
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setNombre(nombre);
        registerRequest.setApellido1(apellido1);
        registerRequest.setApellido2(apellido2);
        registerRequest.setCorreo(correo);
        registerRequest.setTelefono(telefono);
        registerRequest.setDireccion(direccion);
        registerRequest.setContrasena(contrasena);
        registerRequest.setRole(rolNombre);

        // Llamada al método register con dos parámetros (RegisterRequest y rolNombre)
        authService.register(registerRequest, rolNombre);

        System.out.println("Usuario registrado con nombre: " + nombre + " y rol: " + rolNombre);

        // Crear objeto LoginRequest para autenticar al usuario
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setCorreo(correo);
        loginRequest.setContrasena(contrasena);

        // Autenticación para obtener el token
        return Objects.requireNonNull(authService.login(loginRequest).getBody()).getToken();
    }
}

