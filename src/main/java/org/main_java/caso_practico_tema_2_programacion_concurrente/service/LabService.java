package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Experiment;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Lab;
import org.main_java.caso_practico_tema_2_programacion_concurrente.model.LabDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ExperimentRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.LabRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LabService {

    private final LabRepository labRepository;
    private final ExperimentRepository experimentRepository;

    public LabService(LabRepository labRepository, ExperimentRepository experimentRepository) {
        this.labRepository = labRepository;
        this.experimentRepository = experimentRepository;
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
                .orElseThrow(() -> new NotFoundException("Laboratorio no encontrado"));
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
                .orElseThrow(() -> new NotFoundException("Laboratorio no encontrado"));
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






}
