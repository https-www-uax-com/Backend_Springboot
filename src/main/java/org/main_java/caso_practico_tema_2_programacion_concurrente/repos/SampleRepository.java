package org.main_java.caso_practico_tema_2_programacion_concurrente.repos;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
}

