package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "Experimentos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String experimentName;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL)
    private List<Sample> samples;  // Relación con las muestras

}