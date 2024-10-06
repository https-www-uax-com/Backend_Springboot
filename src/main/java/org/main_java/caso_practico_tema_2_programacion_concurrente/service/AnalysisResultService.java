package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

public class AnalysisResultService {
    private final AnalysisResultRepository analysisResultRepository;

    public AnalysisResultService(final AnalysisResultRepository analysisResultRepository) {
        this.analysisResultRepository = analysisResultRepositoryRepository;
    }

    public List<AnalysisResultDTO> findAll() {
        final List<AnalysisResult> analysisResultList = analysisResultRepository.findAll(Sort.by("id"));
        return analysisResultList.stream()
                .map(AnalysisResult -> mapToDTO(analysisResult, new AnalysisResultDTO()))
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
        analysisResultDTO.setSample(analysisResult.getSample());
        analysisResultDTO.setResult(analysisResult.getResult());
        return analysisResultDTO;
    }

    // Mapeo de DTO.md a entidad
    private AnalysisResult mapToEntity(final AnalysisResultDTO analysisDTO, final AnalysisResult analysisResult) {
        analysisResult.setSample(analysisResultDTO.getSample());
        analysisResult.setResult(analysisResultDTO.getResult());
        return analysisResult;
    }
}
