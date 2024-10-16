package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {

    default Optional<Lab> findFirstLabWithLessThanOrEqual20Experiments() {
        List<Lab> labs = findAll();
        return labs.stream()
                .filter(lab -> lab.getExperiments().size() <= 20)
                .findFirst();
    }
}


