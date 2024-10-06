package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.BiologicalData;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.BiologicalDataRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.SampleRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Sample;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public List<SampleDTO> findAll() {
        final List<Sample> samples = sampleRepository.findAll(Sort.by("id"));
        return samples.stream()
                .map(sample -> mapToDTO(sample, new SampleDTO()))
                .collect(Collectors.toList());
    }

    public SampleDTO get(final Long id) {
        return sampleRepository.findById(id)
                .map(sample -> mapToDTO(sample, new SampleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SampleDTO sampleDTO) {
        Sample sample = new Sample();
        mapToEntity(sampleDTO, sample);
        return sampleRepository.save(sample).getId();
    }

    public void update(final Long id, final SampleDTO sampleDTO) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sampleDTO, sample);
        sampleRepository.save(sample);
    }

    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        sampleRepository.deleteById(id);
    }

    private SampleDTO mapToDTO(final Sample sample, final SampleDTO sampleDTO) {
        // Asignamos el ID de la entidad Sample al DTO.md
        sampleDTO.setId(sample.getId());

        // Asignamos el código de la muestra [SampleCode] de la entidad Sample al DTO.md
        sampleDTO.setSampleCode(sample.getSampleCode());

        // Asignamos el ID del experimento relacionado [Experiment] al DTO.md
        sampleDTO.setExperimentId(sample.getExperiment().getId());

        // Creación de un nuevo DTO.md para BiologicalData y mapeo de sus propiedades
        BiologicalDataDTO biologicalDataDTO = new BiologicalDataDTO();

        // Asignamos el ID del BiologicalData relacionado al DTO.md
        biologicalDataDTO.setId(sample.getBiologicalData().getId());

        // Asignamos el tipo de muestra [SampleType] de BiologicalData al DTO.md
        biologicalDataDTO.setSampleType(sample.getBiologicalData().getSampleType());

        // Asignamos los datos específicos de la muestra al DTO.md
        biologicalDataDTO.setData(sample.getBiologicalData().getData());

        // Asignamos el timestamp del BiologicalData al DTO.md
        biologicalDataDTO.setTimestamp(sample.getBiologicalData().getTimestamp());

        // Asignamos el BiologicalDataDTO recien creado al SampleDTO
        sampleDTO.setBiologicalData(biologicalDataDTO);

        return sampleDTO;
    }



    private Sample mapToEntity(final SampleDTO sampleDTO, final Sample sample) {
        // Asignamos el codigo de la muestra desde el DTO.md a la entidad Sample
        sample.setSampleCode(sampleDTO.getSampleCode());

        // Si el experimentId esta presente en el DTO.md, buscamos el Experiment relacionado
        final Experiment experiment = sampleDTO.getExperimentId() == null
                // Si no hay experimentId en el DTO.md, establecemos experiment como null
                ? null
                // Si el experimentId esta presente, buscamos el Experiment en la base de datos
                : experimentRepository.findById(sampleDTO.getExperimentId())
                .orElseThrow(() -> new NotFoundException("Experiment no encontrado"));
        // Asignamos el Experiment encontrado [o null] a la entidad Sample
        sample.setExperiment(experiment);

        // Si los datos biologicos estan presentes en el DTO.md, los mapeamos
        final BiologicalData biologicalData = sampleDTO.getBiologicalData() == null
                // Si no hay BiologicalData en el DTO.md, establecemos biologicalData como null
                ? null
                // Si hay datos biologicos, los buscamos en la base de datos por su ID
                : biologicalDataRepository.findById(sampleDTO.getBiologicalData().getId())
                .orElseThrow(() -> new NotFoundException("Datos biológicos no encontrados"));
        // Asignamos el BiologicalData encontrado [o null] a la entidad Sample
        sample.setBiologicalData(biologicalData);

        return sample;
    }
}
