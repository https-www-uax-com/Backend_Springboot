package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

@Entity
@Table(name= "Credenciale")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter

public class Credenciales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
}