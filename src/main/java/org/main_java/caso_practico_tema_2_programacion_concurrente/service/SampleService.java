package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.BiologicalData;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Sample;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.BiologicalDataRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.SampleRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SampleService {

    private final SampleRepository sampleRepository;
    private final ExperimentRepository experimentRepository;
    private final BiologicalDataRepository biologicalDataRepository;

    public SampleService(SampleRepository sampleRepository, ExperimentRepository experimentRepository, BiologicalDataRepository biologicalDataRepository) {
        this.sampleRepository = sampleRepository;
        this.experimentRepository = experimentRepository;
        this.biologicalDataRepository = biologicalDataRepository;
    }

    // Método para obtener todas las muestras en orden
    public List<SampleDTO> findAll() {
        final List<Sample> samples = sampleRepository.findAll(Sort.by("id"));
        return samples.stream()
                .map(sample -> mapToDTO(sample, new SampleDTO()))
                .collect(Collectors.toList());
    }

    // Método para obtener una muestra por su ID
    public SampleDTO get(final Long id) {
        return sampleRepository.findById(id)
                .map(sample -> mapToDTO(sample, new SampleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    // Método para crear una nueva muestra
    public Long create(final SampleDTO sampleDTO) {
        Sample sample = new Sample();
        mapToEntity(sampleDTO, sample);
        return sampleRepository.save(sample).getId();
    }

    // Método para actualizar una muestra existente
    public void update(final Long id, final SampleDTO sampleDTO) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sampleDTO, sample);
        sampleRepository.save(sample);
    }

    // Método para eliminar una muestra por su ID
    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        sampleRepository.deleteById(id);
    }

    // Método para mapear de entidad Sample a DTO
    private SampleDTO mapToDTO(final Sample sample, final SampleDTO sampleDTO) {
        // Asignamos el ID de la entidad Sample al DTO
        sampleDTO.setId(sample.getId());

        // Asignamos los atributos específicos de Sample
        sampleDTO.setUspCategory(sample.getUspCategory());
        sampleDTO.setUspClass(sample.getUspClass());
        sampleDTO.setUspDrug(sample.getUspDrug());
        sampleDTO.setKeggIdDrug(sample.getKeggIdDrug());
        sampleDTO.setDrugExample(sample.getDrugExample());
        sampleDTO.setNomenclature(sample.getNomenclature());

        // Asignamos el ID del experimento relacionado
        if (sample.getExperiment() != null) {
            sampleDTO.setExperimentId(sample.getExperiment().getId());
        }

        // Mapeamos los datos biológicos relacionados si existen
        if (sample.getBiologicalData() != null) {
            BiologicalDataDTO biologicalDataDTO = new BiologicalDataDTO();
            biologicalDataDTO.setId(sample.getBiologicalData().getId());
            biologicalDataDTO.setSampleType(sample.getBiologicalData().getSampleType());
            biologicalDataDTO.setData(sample.getBiologicalData().getData());
            biologicalDataDTO.setTimestamp(sample.getBiologicalData().getTimestamp());
            sampleDTO.setBiologicalData(biologicalDataDTO);
        }

        return sampleDTO;
    }

    // Método para obtener todas las muestras en orden de forma asincrónica
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<SampleDTO>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Sample> samples = sampleRepository.findAll();
            return samples.stream()
                    .map(this::mapToDTO) // Mapeo a SampleDTO
                    .collect(Collectors.toList());
        });
    }

    // Método para obtener muestras por el ID de experimento de forma asincrónica
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<SampleDTO>> findByExperimentIdAsync(Long experimentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Sample> samples = sampleRepository.findByExperimentId(experimentId);
            return samples.stream()
                    .map(this::mapToDTO) // Mapeo a SampleDTO
                    .collect(Collectors.toList());
        });
    }

    // Método para procesar muestras de manera concurrente
    public void processSamplesConcurrently() {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            pool.submit(() -> {
                List<SampleDTO> samples = findAllAsync().join(); // Obtener todas las muestras de manera concurrente
                samples.parallelStream().forEach(sampleDTO -> {
                    Sample sample = sampleRepository.findById(sampleDTO.getId())
                            .orElseThrow(() -> new NotFoundException("Sample no encontrada"));
                    processSample(sample); // Procesar cada muestra
                });
            }).get();
        } catch (Exception e) {
            System.err.println("Error en el procesamiento concurrente: " + e.getMessage());
        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para procesar una muestra de forma sincrónica
    @Transactional
    public void processSample(Sample sample) {
        // Obtener datos biológicos relacionados con la muestra
        BiologicalData biologicalData = sample.getBiologicalData();
        if (biologicalData == null || biologicalData.getData().isEmpty()) {
            throw new RuntimeException("Datos biológicos faltantes o incompletos para la muestra.");
        }

        // Simular proceso de análisis de los datos biológicos
        String analysisResult = analyzeBiologicalData(biologicalData.getData());

        // Actualizar los resultados del análisis en la entidad BiologicalData
        biologicalData.setAnalysisResult(analysisResult);
        biologicalDataRepository.save(biologicalData);

        System.out.println("Resultado del análisis para la muestra: " + analysisResult);
    }

    // Simulación de análisis de los datos biológicos
    private String analyzeBiologicalData(String data) {
        int dataLength = data.length();
        if (dataLength > 50) {
            return "Datos óptimos - análisis satisfactorio.";
        } else if (dataLength > 20) {
            return "Datos suficientes - análisis realizado con éxito.";
        } else {
            return "Datos insuficientes - análisis incompleto.";
        }
    }

    // Método para mapear de entidad Sample a DTO
    public SampleDTO mapToDTO(final Sample sample) {
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.setId(sample.getId());
        sampleDTO.setUspCategory(sample.getUspCategory());
        sampleDTO.setUspClass(sample.getUspClass());
        sampleDTO.setUspDrug(sample.getUspDrug());
        sampleDTO.setKeggIdDrug(sample.getKeggIdDrug());
        sampleDTO.setDrugExample(sample.getDrugExample());
        sampleDTO.setNomenclature(sample.getNomenclature());

        if (sample.getExperiment() != null) {
            sampleDTO.setExperimentId(sample.getExperiment().getId());
        }

        if (sample.getBiologicalData() != null) {
            BiologicalDataDTO biologicalDataDTO = new BiologicalDataDTO();
            biologicalDataDTO.setId(sample.getBiologicalData().getId());
            biologicalDataDTO.setSampleType(sample.getBiologicalData().getSampleType());
            biologicalDataDTO.setData(sample.getBiologicalData().getData());
            biologicalDataDTO.setTimestamp(sample.getBiologicalData().getTimestamp());
            sampleDTO.setBiologicalData(biologicalDataDTO);
        }

        return sampleDTO;
    }

    // Método para mapear de DTO a entidad Sample
    public Sample mapToEntity(final SampleDTO sampleDTO, final Sample sample) {
        sample.setUspCategory(sampleDTO.getUspCategory());
        sample.setUspClass(sampleDTO.getUspClass());
        sample.setUspDrug(sampleDTO.getUspDrug());
        sample.setKeggIdDrug(sampleDTO.getKeggIdDrug());
        sample.setDrugExample(sampleDTO.getDrugExample());
        sample.setNomenclature(sampleDTO.getNomenclature());

        // Si el experimentId está presente en el DTO, buscamos el Experiment relacionado
        final Experiment experiment = sampleDTO.getExperimentId() == null
                ? null
                : experimentRepository.findById(sampleDTO.getExperimentId())
                .orElseThrow(() -> new NotFoundException("Experiment no encontrado"));
        sample.setExperiment(experiment);

        // Si los datos biológicos están presentes en el DTO, los mapeamos
        final BiologicalData biologicalData = sampleDTO.getBiologicalData() == null
                ? null
                : biologicalDataRepository.findById(sampleDTO.getBiologicalData().getId())
                .orElseThrow(() -> new NotFoundException("Datos biológicos no encontrados"));
        sample.setBiologicalData(biologicalData);

        return sample;
    }

    public void save(SampleDTO sampleDTO) {
        Sample sample = new Sample();
        mapToEntity(sampleDTO, sample);
        sampleRepository.save(sample);
    }
}

