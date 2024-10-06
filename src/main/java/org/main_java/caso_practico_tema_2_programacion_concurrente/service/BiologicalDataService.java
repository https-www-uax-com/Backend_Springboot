package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.BiologicalDataDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.BiologicalDataRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.BiologicalData;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BiologicalDataService {

    private final BiologicalDataRepository biologicalDataRepository;

    public BiologicalDataService(final BiologicalDataRepository biologicalDataRepository) {
        this.biologicalDataRepository = biologicalDataRepository;
    }

    public List<BiologicalDataDTO> findAll() {
        final List<BiologicalData> biologicalDataList = biologicalDataRepository.findAll(Sort.by("id"));
        return biologicalDataList.stream()
                .map(biologicalData -> mapToDTO(biologicalData, new BiologicalDataDTO()))
                .collect(Collectors.toList());
    }

    public BiologicalDataDTO get(final Long id) {
        return biologicalDataRepository.findById(id)
                .map(biologicalData -> mapToDTO(biologicalData, new BiologicalDataDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BiologicalDataDTO biologicalDataDTO) {
        BiologicalData biologicalData = new BiologicalData();
        mapToEntity(biologicalDataDTO, biologicalData);
        return biologicalDataRepository.save(biologicalData).getId();
    }

    public void update(final Long id, final BiologicalDataDTO biologicalDataDTO) {
        BiologicalData biologicalData = biologicalDataRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(biologicalDataDTO, biologicalData);
        biologicalDataRepository.save(biologicalData);
    }

    public void delete(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null");
        }
        biologicalDataRepository.deleteById(id);
    }

    // Mapeo de entidad a DTO.md
    private BiologicalDataDTO mapToDTO(final BiologicalData biologicalData, final BiologicalDataDTO biologicalDataDTO) {
        biologicalDataDTO.setId(biologicalData.getId());
        biologicalDataDTO.setSampleType(biologicalData.getSampleType());
        biologicalDataDTO.setData(biologicalData.getData());
        biologicalDataDTO.setTimestamp(biologicalData.getTimestamp());
        return biologicalDataDTO;
    }

    // Mapeo de DTO.md a entidad
    private BiologicalData mapToEntity(final BiologicalDataDTO biologicalDataDTO, final BiologicalData biologicalData) {
        biologicalData.setSampleType(biologicalDataDTO.getSampleType());
        biologicalData.setData(biologicalDataDTO.getData());
        biologicalData.setTimestamp(biologicalDataDTO.getTimestamp());
        return biologicalData;
    }
}

