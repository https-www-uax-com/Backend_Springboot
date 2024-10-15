package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name= "BiologicalDatas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BiologicalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sampleType;  // Tipo de muestra: [genética, bioquímica, física]

    @Column(nullable = false)
    private String data;        // Datos específicos de la muestra

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = true)
    private String analysisResult;  // Nuevo campo para el resultado del análisis

    @OneToOne(mappedBy = "biologicalData", cascade = CascadeType.ALL)
    private Sample sample;

}
