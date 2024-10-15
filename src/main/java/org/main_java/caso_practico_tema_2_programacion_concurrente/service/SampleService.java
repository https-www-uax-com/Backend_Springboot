package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.*;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.*;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SampleService {

    private final SampleRepository sampleRepository;
    private final ExperimentRepository experimentRepository;
    private final BiologicalDataRepository biologicalDataRepository;
    private final LabRepository labRepository;
    private final ResearcherRepository researcherRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ExecutorService executorService2 = Executors.newFixedThreadPool(100);

    private static AtomicInteger experimentCounter = new AtomicInteger(1);
    private static AtomicInteger labCounter = new AtomicInteger(1);
    private static AtomicInteger researcherCounter = new AtomicInteger(1);

    public SampleService(SampleRepository sampleRepository, ExperimentRepository experimentRepository, BiologicalDataRepository biologicalDataRepository, LabRepository labRepository, ResearcherRepository researcherRepository) {
        this.sampleRepository = sampleRepository;
        this.experimentRepository = experimentRepository;
        this.biologicalDataRepository = biologicalDataRepository;
        this.labRepository = labRepository;
        this.researcherRepository = researcherRepository;
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

    @Transactional
    public void processSamplesConcurrently() {
        List<SampleDTO> samples = findAllAsync().join();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (SampleDTO sampleDTO : samples) {
            futures.add(CompletableFuture.runAsync(() -> {
                Sample sample = sampleRepository.findById(sampleDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Sample no encontrada"));
                processSample(sample);  // Procesar cada muestra
            }, executorService2));
        }

        // Asegurar que todos los futuros se completen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Async
    @Transactional
    public CompletableFuture<Void> processSingleSample(Long id) {
        return CompletableFuture.runAsync(() -> {
            Sample sample = sampleRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Sample no encontrada"));
            processSample(sample);  // Delegar a processSample para hacer el procesamiento
        }, executorService);
    }

    @Transactional
    public void processSample(Sample sample) {
        try {
            Sample managedSample = sampleRepository.findById(sample.getId())
                    .orElseThrow(() -> new NotFoundException("Muestra no encontrada con ID: " + sample.getId()));

            BiologicalData biologicalData = managedSample.getBiologicalData();
            if (biologicalData == null) {
                biologicalData = new BiologicalData();
                biologicalData.setSampleType("TYPE_DATOS_UNDF");
                biologicalData.setData("Datos iniciales de la muestra");
                biologicalData.setTimestamp(LocalDateTime.now());
                biologicalData.setAnalysisResult("PENDIENTES");
                biologicalData = biologicalDataRepository.save(biologicalData);
                managedSample.setBiologicalData(biologicalData);
            }

            // Verificar si la muestra ya está asociada a un experimento
            if (managedSample.getExperiment() == null) {
                System.out.println("La muestra ID: " + managedSample.getId() + " no está asociada a ningún experimento. Asignando...");
                synchronized (this) {
                    assignExperimentToSample(managedSample);
                }
            } else {
                System.out.println("La muestra ID: " + managedSample.getId() + " ya está asociada al experimento ID: " + managedSample.getExperiment().getId());
            }

            sampleRepository.save(managedSample);
            System.out.println("Procesamiento completado para la muestra ID: " + managedSample.getId());
        } catch (Exception e) {
            System.err.println("Error procesando la muestra ID: " + sample.getId() + " - " + e.getMessage());
        }
    }
    @Transactional
    public void assignExperimentToSample(Sample sample) {
        Experiment experiment;

        synchronized (this) {
            // Buscamos todos los experimentos
            List<Experiment> experiments = experimentRepository.findAllExperiments();

            // Filtramos los que tienen menos de 20 muestras y que ya tienen muestras
            Optional<Experiment> optionalExperiment = experiments.stream()
                    .filter(exp -> exp.getSamples().size() < 20 && !exp.getSamples().isEmpty())
                    .findFirst();

            // Si no encontramos un experimento con menos de 20 muestras, creamos uno nuevo
            experiment = optionalExperiment.orElseGet(this::createNewExperiment);
        }

        sample.setExperiment(experiment);  // Asignar el experimento a la muestra
    }

    private Experiment createNewExperiment() {
        Lab lab = labRepository.findFirstLabWithLessThanOrEqual20Experiments().orElseGet(this::createNewLab);
        Researcher researcher = researcherRepository.findFirstResearcherWithLessThanOrEqual10Experiments().orElseGet(this::createNewResearcher);

        Experiment newExperiment = new Experiment();
        newExperiment.setExperimentName("Experimento " + experimentCounter.getAndIncrement());
        newExperiment.setStartDate(LocalDate.now().atStartOfDay());
        newExperiment.setEndDate(LocalDate.now().atStartOfDay().plusMonths(3));
        newExperiment.setStatus("EN_ACTIVO");
        newExperiment.setLab(lab);
        newExperiment.setResearcher(researcher);
        newExperiment.setSamples(new ArrayList<>());

        return experimentRepository.save(newExperiment);
    }

    private Lab createNewLab() {
        Lab lab = new Lab();
        lab.setLabName("Laboratorio " + labCounter.getAndIncrement());
        lab.setLocation("Ubicación desconocida");
        return labRepository.save(lab);
    }

    private Researcher createNewResearcher() {
        Researcher researcher = new Researcher();
        researcher.setName("Investigador " + experimentCounter.getAndIncrement());
        researcher.setSpecialty("Especialidad desconocida");
        return researcherRepository.save(researcher);
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

