package com.api.vetlens.service;

import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.rest.MachineLearningClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class InferenceService {

    private final MachineLearningClientImpl client = new MachineLearningClientImpl();

    public PredictionDTO makeInference(MultipartFile image) {
        return client.makeInference(image);
    }
}
