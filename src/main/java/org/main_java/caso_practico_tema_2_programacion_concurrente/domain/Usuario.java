package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

@Entity
@Table(name= "Usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String nombre;
}