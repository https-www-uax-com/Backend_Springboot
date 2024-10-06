package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ExperimentDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperimentService {

    private final ExperimentRepository experimentRepository;

    public ExperimentService(final ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    public List<ExperimentDTO> findAll() {
        final List<Experiment> experiments = experimentRepository.findAll(Sort.by("id"));
        return experiments.stream()
                .map(experiment -> mapToDTO(experiment, new ExperimentDTO()))
                .collect(Collectors.toList());
    }

    public ExperimentDTO get(final Long id) {
        return experimentRepository.findById(id)
                .map(experiment -> mapToDTO(experiment, new ExperimentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ExperimentDTO experimentDTO) {
        Experiment experiment = new Experiment();
        mapToEntity(experimentDTO, experiment);
        return experimentRepository.save(experiment).getId();
    }

    public void update(final Long id, final ExperimentDTO experimentDTO) {
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(experimentDTO, experiment);
        experimentRepository.save(experiment);
    }

    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        experimentRepository.deleteById(id);
    }

    // Mapeo de entidad a DTO
    private ExperimentDTO mapToDTO(final Experiment experiment, final ExperimentDTO experimentDTO) {
        experimentDTO.setId(experiment.getId());
        experimentDTO.setExperimentName(experiment.getExperimentName()); // Asignamos el nombre a el experimento
        experimentDTO.setStartDate(experiment.getStartDate());
        experimentDTO.setEndDate(experiment.getEndDate());
        return experimentDTO;
    }

    // Mapeo de DTO a entidad
    private Experiment mapToEntity(final ExperimentDTO experimentDTO, final Experiment experiment) {
        experiment.setExperimentName(experimentDTO.getExperimentName());
        experiment.setStartDate(experimentDTO.getStartDate());
        experiment.setEndDate(experimentDTO.getEndDate());
        return experiment;
    }
}
