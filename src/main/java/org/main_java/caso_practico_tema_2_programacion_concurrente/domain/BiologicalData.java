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

    private String sampleType;  // Tipo de muestra: [genética, bioquímica, física]
    private String data;        // Datos específicos de la muestra
    private LocalDateTime timestamp;

}
