package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {

    @Query("SELECT e FROM Experiment e LEFT JOIN FETCH e.samples")
    List<Experiment> findAllExperiments();


    @Query("SELECT e FROM Experiment e JOIN FETCH e.samples WHERE e.id = :experimentId")
    Optional<Experiment> findExperimentWithSamples(@Param("experimentId") Long experimentId);

}
