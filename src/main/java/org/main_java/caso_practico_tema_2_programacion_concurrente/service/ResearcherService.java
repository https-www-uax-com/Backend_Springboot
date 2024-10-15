package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ResearcherDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ResearcherRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Researcher;
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
public class ResearcherService {

    private final ResearcherRepository researcherRepository;

    private final ExecutorService executor = Executors.newFixedThreadPool(5);


    public ResearcherService(final ResearcherRepository researcherRepository) {
        this.researcherRepository = researcherRepository;
    }

    public List<ResearcherDTO> findAll() {
        final List<Researcher> researchers = researcherRepository.findAll(Sort.by("id"));
        return researchers.stream()
                .map(researcher -> mapToDTO(researcher, new ResearcherDTO()))
                .collect(Collectors.toList());
    }

    public ResearcherDTO get(final Long id) {
        return researcherRepository.findById(id)
                .map(researcher -> mapToDTO(researcher, new ResearcherDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Researcher getResearcherEntity(Long id) {
        return researcherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Researcher no encontrado"));
    }


    public Long create(final ResearcherDTO researcherDTO) {
        Researcher researcher = new Researcher();
        mapToEntity(researcherDTO, researcher);
        return researcherRepository.save(researcher).getId();
    }

    public void update(final Long id, final ResearcherDTO researcherDTO) {
        Researcher researcher = researcherRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(researcherDTO, researcher);
        researcherRepository.save(researcher);
    }

    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        researcherRepository.deleteById(id);
    }

    // Mapeo de entidad a DTO.md
    private ResearcherDTO mapToDTO(final Researcher researcher, final ResearcherDTO researcherDTO) {
        researcherDTO.setId(researcher.getId());
        researcherDTO.setName(researcher.getName());
        researcherDTO.setSpecialty(researcher.getSpecialty());
        return researcherDTO;
    }

    // Mapeo de DTO.md a entidad
    private Researcher mapToEntity(final ResearcherDTO researcherDTO, final Researcher researcher) {
        researcher.setName(researcherDTO.getName());
        researcher.setSpecialty(researcherDTO.getSpecialty());
        return researcher;
    }

    // Método asincrónico para obtener todos los investigadores
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<ResearcherDTO>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Researcher> researchers = researcherRepository.findAll();
            return researchers.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }, executor);  // Utilizamos el pool de hilos "executor" aquí
    }

    // Procesar investigadores de manera asincrónica usando un pool de hilos
    @Async
    @Transactional
    public CompletableFuture<Void> processResearcherAsync(Researcher researcher) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Procesando investigador de manera asincrónica: " + researcher.getName());
            processResearcher(researcher);
        }, executor);  // Utilizamos el pool de hilos "executor" aquí
    }

    // Lógica sincrónica para procesar un investigador (usada por processResearcherAsync)
    @Transactional
    public void processResearcher(Researcher researcher) {
        System.out.println("Procesando investigador: " + researcher.getName());

        // Verificar la cantidad de experimentos que ha supervisado
        int totalExperiments = researcher.getExperiments().size();
        System.out.println("El investigador " + researcher.getName() + " ha supervisado " + totalExperiments + " experimentos.");

        // Procesar los experimentos del investigador
        researcher.getExperiments().forEach(this::processExperiment);
    }

    // Simulación del procesamiento de un experimento supervisado por el investigador
    private void processExperiment(Experiment experiment) {
        System.out.println("Procesando experimento supervisado: " + experiment.getExperimentName());
        try {
            // Simular el procesamiento con una pausa de 2 segundos
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al procesar el experimento: " + experiment.getExperimentName());
        }
        System.out.println("Experimento " + experiment.getExperimentName() + " procesado exitosamente.");
    }

    // Mapeo de entidad Researcher a DTO
    private ResearcherDTO mapToDTO(final Researcher researcher) {
        ResearcherDTO researcherDTO = new ResearcherDTO();
        researcherDTO.setId(researcher.getId());
        researcherDTO.setName(researcher.getName());
        researcherDTO.setSpecialty(researcher.getSpecialty());
        return researcherDTO;
    }

    // Llamar a shutdown() cuando todos los procesos han terminado
    public void finalizeProcessing() {
        // Aquí podrías llamar al shutdown una vez que todas las tareas han sido ejecutadas
        shutdown();
    }

    // Cierre del ExecutorService para liberar recursos
    public void shutdown() {
        executor.shutdown();  // Iniciar el cierre del pool de hilos
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();  // Forzar el cierre si no termina en el tiempo esperado
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService no se cerró.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();  // Forzar el cierre en caso de interrupción
            Thread.currentThread().interrupt();
        }
    }
}

