package org.main_java.caso_practico_tema_2_programacion_concurrente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.List;

@Entity
@Table(name= "Labs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labName;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL)
    private List<Experiment> experiments;

}

