package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Sample;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.AnalysisResultDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.AnalysisResultRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.AnalysisResult;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.SampleRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AnalysisResultService {

    @Autowired
    private SampleService sampleService;

    private final AnalysisResultRepository analysisResultRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public AnalysisResultService(final AnalysisResultRepository analysisResultRepository) {
        this.analysisResultRepository = analysisResultRepository;
    }

    public List<AnalysisResultDTO> findAll() {
        final List<AnalysisResult> analysisResults = analysisResultRepository.findAll(Sort.by("id"));
        return analysisResults.stream()
                .map(analysisResult -> mapToDTO(analysisResult, new AnalysisResultDTO()))
                .collect(Collectors.toList());
    }

    public AnalysisResultDTO get(final Long id) {
        return analysisResultRepository.findById(id)
                .map(analysisResult -> mapToDTO(analysisResult, new AnalysisResultDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AnalysisResultDTO analysisResultDTO) {
        AnalysisResult analysisResult = new AnalysisResult();
        mapToEntity(analysisResultDTO, analysisResult);
        return analysisResultRepository.save(analysisResult).getId();
    }

    public void update(final Long id, final AnalysisResultDTO analysisResultDTO) {
        AnalysisResult analysisResult = analysisResultRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(analysisResultDTO, analysisResult);
        analysisResultRepository.save(analysisResult);
    }

    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        analysisResultRepository.deleteById(id);
    }

    // Mapeo de entidad a DTO.md
    private AnalysisResultDTO mapToDTO(final AnalysisResult analysisResult, final AnalysisResultDTO analysisResultDTO) {
        analysisResultDTO.setId(analysisResult.getId());
        analysisResultDTO.setResult(analysisResult.getResult());
        return analysisResultDTO;
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<AnalysisResultDTO>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<AnalysisResult> analysisResults = analysisResultRepository.findAll();
            return analysisResults.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }, executor);
    }

    // Método para procesar resultados de análisis concurrentemente usando ExecutorService
    public void processAnalysisResultsConcurrently() {
        List<AnalysisResult> analysisResults = analysisResultRepository.findAll();

        analysisResults.parallelStream().forEach(analysisResult -> {
            processSingleAnalysisResult(analysisResult);
        });

        // Al terminar el procesamiento, cerrar el executor
        shutdownExecutor();
    }

    // Lógica para procesar un solo resultado de análisis
    @Transactional
    public synchronized void processSingleAnalysisResult(AnalysisResult analysisResult) {
        System.out.println("Procesando resultado del análisis para la muestra con ID: " + analysisResult.getSample().getId());

        try {
            Sample sample = analysisResult.getSample();

            // Simulación del análisis del resultado
            String newResult = analyzeResult(sample);

            // Actualizar el resultado en la base de datos
            analysisResult.setResult(newResult);
            analysisResultRepository.save(analysisResult);
            System.out.println("Resultado del análisis procesado correctamente: " + newResult);

        } catch (Exception e) {
            System.err.println("Error al procesar el resultado del análisis: " + analysisResult.getSample().getId() + ". Detalle: " + e.getMessage());
        }
    }

    // Simulación de análisis de resultados de una muestra
    private String analyzeResult(Sample sample) {
        String uspCategory = sample.getUspCategory();
        String uspClass = sample.getUspClass();

        // Simular un análisis basado en los atributos de la muestra
        if (uspCategory != null && uspCategory.equalsIgnoreCase("category1")) {
            return "Resultado óptimo para la categoría 1.";
        } else if (uspClass != null && uspClass.equalsIgnoreCase("classA")) {
            return "Resultado satisfactorio para la clase A.";
        } else {
            return "Resultado estándar.";
        }
    }

    // Cierre del ExecutorService para liberar recursos
    public void shutdownExecutor() {
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

    // Mapeo de entidad a DTO
    private AnalysisResultDTO mapToDTO(final AnalysisResult analysisResult) {
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();
        analysisResultDTO.setId(analysisResult.getId());
        analysisResultDTO.setResult(analysisResult.getResult());

        // Mapeamos el Sample a SampleDTO utilizando el SampleService
        SampleDTO sampleDTO = sampleService.mapToDTO(analysisResult.getSample());  // Llamada corregida
        analysisResultDTO.setSample(sampleDTO);

        return analysisResultDTO;
    }


    // Mapeo de DTO a entidad
    public AnalysisResult mapToEntity(final AnalysisResultDTO analysisResultDTO, final AnalysisResult analysisResult) {
        analysisResult.setResult(analysisResultDTO.getResult());

        // Mapeamos el SampleDTO a Sample utilizando el SampleService
        Sample sample = sampleService.mapToEntity(analysisResultDTO.getSample(), new Sample());
        analysisResult.setSample(sample);

        return analysisResult;
    }
}
