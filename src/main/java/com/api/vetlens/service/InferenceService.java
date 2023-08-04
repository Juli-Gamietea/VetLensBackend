package com.api.vetlens.service;

import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.entity.Anamnesis;
import com.api.vetlens.entity.Inference;
import com.api.vetlens.repository.InferenceRepository;
import com.api.vetlens.rest.MachineLearningClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InferenceService {
    private final MachineLearningClientImpl client = new MachineLearningClientImpl();
    private final DiseaseService diseaseService;
    private final InferenceRepository inferenceRepository;
    private final Float pyotraumaticDermatitisThreshold = 0f;
    private final Float dermathophytosisThreshold = 0f;
    private final Float myasisThreshold = 0f;
    private final Float othersThreshold = 0f;

    public Anamnesis makeInference(MultipartFile image, Anamnesis anamnesis) {

        List<Inference> inferences = new ArrayList<>();
        PredictionDTO prediction = client.makeInference(image);

        if (prediction.getDermatophytosis() >= dermathophytosisThreshold){
            Inference dermatophytosis = new Inference();
            dermatophytosis.setProbability(formatNumber(prediction.getDermatophytosis()));
            dermatophytosis.setAnamnesis(anamnesis);
            dermatophytosis.setDisease(diseaseService.getDisease(3));
            inferenceRepository.save(dermatophytosis);
            inferences.add(dermatophytosis);
        }
        if (prediction.getMyasis() >= myasisThreshold){
            Inference myasis = new Inference();
            myasis.setProbability(formatNumber(prediction.getMyasis()));
            myasis.setAnamnesis(anamnesis);
            myasis.setDisease(diseaseService.getDisease(2));
            inferenceRepository.save(myasis);
            inferences.add(myasis);
        }
        if (prediction.getPyotraumaticDermatitis() >= pyotraumaticDermatitisThreshold){
            Inference pyotraumaticDermatitis = new Inference();
            pyotraumaticDermatitis.setProbability(formatNumber(prediction.getPyotraumaticDermatitis()));
            pyotraumaticDermatitis.setAnamnesis(anamnesis);
            pyotraumaticDermatitis.setDisease(diseaseService.getDisease(1));
            inferenceRepository.save(pyotraumaticDermatitis);
            inferences.add(pyotraumaticDermatitis);
        }
        if (prediction.getOthers() >= othersThreshold){
            Inference others = new Inference();
            others.setProbability(formatNumber(prediction.getOthers()));
            others.setAnamnesis(anamnesis);
            others.setDisease(diseaseService.getDisease(4));
            inferenceRepository.save(others);
            inferences.add(others);
        }
        anamnesis.setInferences(inferences);
        return anamnesis;
    }

    private String formatNumber(Float number) {
        DecimalFormat newFormat = new DecimalFormat("#0.0000");
        return newFormat.format(number);
    }
}
