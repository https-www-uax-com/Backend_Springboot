package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name= "Usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String nombre;

    @Column(nullable = false)
    private String apellido1;

    @Column(nullable = false)
    private String apellido2;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private Integer telefono;

    @Column
    private String direccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarios_id", nullable = false)
    private Rol usuarios;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Credenciales usuario;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

}