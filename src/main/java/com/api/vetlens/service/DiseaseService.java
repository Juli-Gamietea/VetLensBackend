package com.api.vetlens.service;

import com.api.vetlens.dto.diagnosis.DiseaseDTO;
import com.api.vetlens.entity.Disease;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;
    private final ModelMapper mapper = new ModelMapper();
    public Disease getDisease(Integer id) {
        Optional<Disease> disease = diseaseRepository.findById(id);
        if (disease.isEmpty()) {
            throw new NotFoundException("Enfermedad no encontrada");
        }
        return disease.get();
    }

}
