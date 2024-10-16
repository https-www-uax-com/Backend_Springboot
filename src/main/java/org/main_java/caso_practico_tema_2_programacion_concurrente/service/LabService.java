package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Lab;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.LabDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.LabRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class LabService {

    private final LabRepository labRepository;
    //private final ExperimentRepository experimentRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final ExperimentService experimentService;


    public LabService(LabRepository labRepository, ExperimentRepository experimentRepository, ExperimentService experimentService) {
        this.labRepository = labRepository;
       //this.experimentRepository = experimentRepository;
        this.experimentService = experimentService;
    }


    // Obtiene una lista de todos los laboratorios
    public List<LabDTO> findAll() {
        final List<Lab> labs = labRepository.findAll(Sort.by("id"));
        return labs.stream()
                .map(lab -> mapToDTO(lab, new LabDTO()))
                .collect(Collectors.toList());
    }


    // Obtiene un laboratorio específico por ID
    public LabDTO get(final Long id) {
        return labRepository.findById(id)
                .map(lab -> mapToDTO(lab, new LabDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Lab getLabEntity(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lab no encontrado"));
    }

    // Crea un nuevo laboratorio
    public Long create(final LabDTO labDTO) {
        Lab lab = new Lab();
        mapToEntity(labDTO, lab);
        return labRepository.save(lab).getId();
    }


    // Actualiza un laboratorio existente
    public void update(final Long id, final LabDTO labDTO) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(labDTO, lab);
        labRepository.save(lab);
    }


    // Elimina un laboratorio por su ID
    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        labRepository.deleteById(id);
    }


    // Mapea de Lab a LabDTO
    private LabDTO mapToDTO(final Lab lab, final LabDTO labDTO) {
        // Asignamos el ID de la entidad Lab al DTO
        labDTO.setId(lab.getId());

        // Asignamos el nombre del laboratorio [LabName] de la entidad Lab al DTO
        labDTO.setLabName(lab.getLabName());

        // Asignamos la ubicación del laboratorio [Location] de la entidad Lab al DTO
        labDTO.setLocation(lab.getLocation());

        return labDTO;
    }


    // Mapea de LabDTO a Lab
    private Lab mapToEntity(final LabDTO labDTO, final Lab lab) {
        // Asignamos el nombre del laboratorio desde el DTO a la entidad Lab
        lab.setLabName(labDTO.getLabName());

        // Asignamos la ubicación del laboratorio desde el DTO a la entidad Lab
        lab.setLocation(labDTO.getLocation());

        return lab;
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<LabDTO>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Lab> labs = labRepository.findAll();
            return labs.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        });
    }

    @Async
    @Transactional
    public CompletableFuture<Void> processLabAsync(Lab lab) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Procesando laboratorio: " + lab.getLabName());
            processLab(lab);
        }, executor);
    }

    @Transactional
    public void processLab(Lab lab) {
        System.out.println("Procesando laboratorio: " + lab.getLabName());

        // Verificar el número de experimentos que tiene el laboratorio
        int totalExperiments = lab.getExperiments().size();
        System.out.println("Laboratorio " + lab.getLabName() + " tiene " + totalExperiments + " experimentos.");

        // Procesar los experimentos de este laboratorio
        lab.getExperiments().forEach(experiment -> {
            experimentService.processSingleExperiment(experimentService.mapToDTO(experiment));
        });
        System.out.println("Los laboratorios han sido procesados con exito.");
    }



    // Método auxiliar para mapear entidad Lab a DTO
    private LabDTO mapToDTO(final Lab lab) {
        LabDTO labDTO = new LabDTO();
        labDTO.setId(lab.getId());
        labDTO.setLabName(lab.getLabName());
        labDTO.setLocation(lab.getLocation());
        return labDTO;
    }

    // Simular el cierre del ExecutorService al finalizar la aplicación
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService no se cerró.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
