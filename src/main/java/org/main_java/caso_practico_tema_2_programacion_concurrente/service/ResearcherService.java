package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.ResearcherDTO;
import org.main_java.caso_practico_tema_2_programacion_concurrente.repos.ResearcherRepository;
import org.main_java.caso_practico_tema_2_programacion_concurrente.domain.Researcher;
import org.main_java.caso_practico_tema_2_programacion_concurrente.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearcherService {

    private final ResearcherRepository researcherRepository;

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
}

