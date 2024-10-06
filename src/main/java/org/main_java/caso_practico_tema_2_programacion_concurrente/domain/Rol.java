package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

@Entity
@Table(name= "Rols")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rol;
}