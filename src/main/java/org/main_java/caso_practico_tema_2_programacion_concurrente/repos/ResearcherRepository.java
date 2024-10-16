package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Researcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResearcherRepository extends JpaRepository<Researcher, Long> {

    default Optional<Researcher> findFirstResearcherWithLessThanOrEqual10Experiments() {
        List<Researcher> researchers= findAll();
        return researchers.stream()
                .filter(researcher-> researcher.getExperiments().size() <= 10)
                .findFirst();
    }

}

