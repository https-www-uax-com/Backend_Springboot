package org.main_java.caso_practico_tema_2_programacion_concurrente.service;

import org.main_java.caso_practico_tema_2_programacion_concurrente.model.SampleDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class CSVProcessingService {

    private final SampleService sampleService;

    public CSVProcessingService(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    // Metodo para procesar un archivo CSV
    public void processCSVFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Suponiendo que el CSV usa comas
                SampleDTO sampleDTO = parseLineToSampleDTO(data);
                sampleService.save(sampleDTO);  // Guardar en la base de datos mediante SampleService
            }
        }
    }

    // Metodo auxiliar para mapear una línea del CSV a SampleDTO
    private SampleDTO parseLineToSampleDTO(String[] data) {
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.setUspCategory(data[0]);
        sampleDTO.setUspClass(data[1]);
        sampleDTO.setUspDrug(data[2]);
        sampleDTO.setKeggIdDrug(data[3]);
        sampleDTO.setDrugExample(data[4]);
        sampleDTO.setNomenclature(data[5]);
        return sampleDTO;
    }
}
