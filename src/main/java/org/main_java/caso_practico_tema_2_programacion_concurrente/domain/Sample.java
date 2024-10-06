package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name= "Samples")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sampleCode;

    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    @OneToOne(cascade = CascadeType.ALL)
    private BiologicalData biologicalData;

}

