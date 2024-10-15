package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Sample;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ExperimentDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ExperimentService {

    private final ExperimentRepository experimentRepository;

    private final SampleRepository sampleRepository;

    @Autowired
    private SampleService sampleService;


    public ExperimentService(final ExperimentRepository experimentRepository, final SampleRepository sampleRepository) {
        this.experimentRepository = experimentRepository;
        this.sampleRepository = sampleRepository;
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

    // Mapeo de entidad a DTO.md
    private ExperimentDTO mapToDTO(final Experiment experiment, final ExperimentDTO experimentDTO) {
        experimentDTO.setId(experiment.getId());
        experimentDTO.setExperimentName(experiment.getExperimentName()); // Asignamos el nombre a el experimento
        experimentDTO.setStartDate(experiment.getStartDate());
        experimentDTO.setEndDate(experiment.getEndDate());
        return experimentDTO;
    }

    // Mapeo de DTO.md a entidad
    private Experiment mapToEntity(final ExperimentDTO experimentDTO, final Experiment experiment) {
        experiment.setExperimentName(experimentDTO.getExperimentName());
        experiment.setStartDate(experimentDTO.getStartDate());
        experiment.setEndDate(experimentDTO.getEndDate());
        return experiment;
    }

    private ExperimentDTO mapToDTO(final Experiment experiment) {
        ExperimentDTO experimentDTO = new ExperimentDTO();
        experimentDTO.setId(experiment.getId());
        experimentDTO.setExperimentName(experiment.getExperimentName());
        experimentDTO.setStartDate(experiment.getStartDate());
        experimentDTO.setEndDate(experiment.getEndDate());
        return experimentDTO;
    }

    private Experiment mapToEntity(final ExperimentDTO experimentDTO) {
        Experiment experiment = new Experiment();
        experiment.setId(experimentDTO.getId());
        experiment.setExperimentName(experimentDTO.getExperimentName());
        experiment.setStartDate(experimentDTO.getStartDate());
        experiment.setEndDate(experimentDTO.getEndDate());
        return experiment;
    }
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<ExperimentDTO>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Experiment> experiments = experimentRepository.findAll();
            return experiments.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        });
    }

    // Lógica para el procesamiento concurrente de experimentos
    public void processExperimentsConcurrently() {
        ForkJoinPool pool = new ForkJoinPool();

        try {
            pool.submit(() -> {
                List<ExperimentDTO> experiments = findAllAsync().join(); // Obtener todos los experimentos
                experiments.parallelStream().forEach(experiment -> {
                    List<Sample> samples = sampleRepository.findByExperimentId(experiment.getId());
                    samples.forEach(sampleService::processSample); // Procesar cada muestra concurrentemente
                });
            }).get();
        } catch (Exception e) {
            System.err.println("Error en el procesamiento concurrente de experimentos: " + e.getMessage());
        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para procesar un solo experimento y sus muestras de forma sincronizada
    @Transactional
    public synchronized void processSingleExperiment(ExperimentDTO experimentDTO) {
        System.out.println("Procesando experimento: " + experimentDTO.getExperimentName());

        try {
            // Obtener muestras relacionadas con el experimento
            List<Sample> samples = sampleRepository.findByExperimentId(experimentDTO.getId());
            samples.forEach(sampleService::processSample); // Procesar cada muestra

            // Actualizar el estado del experimento después del procesamiento
            Experiment experiment = experimentRepository.findById(experimentDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Experiment no encontrado"));
            experimentRepository.save(experiment);
            System.out.println("Experimento procesado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al procesar el experimento: " + experimentDTO.getExperimentName() + ". Detalle: " + e.getMessage());
        }
    }
}
