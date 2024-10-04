package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name= "AnalysisResults")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Sample sample;

    private String result;
}

