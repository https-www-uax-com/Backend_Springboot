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

    @Column
    private String uspCategory;  // Mapea 'usp_category'
    @Column
    private String uspClass;     // Mapea 'usp_class'
    @Column
    private String uspDrug;      // Mapea 'usp_drug'
    @Column
    private String keggIdDrug;   // Mapea 'kegg_id_drug'
    @Column
    private String drugExample;  // Mapea 'drug_example'
    @Column
    private String nomenclature;

    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "biological_data_id")
    private BiologicalData biologicalData;

}

