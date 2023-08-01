package com.api.vetlens.rest;

import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Response;
import feign.form.spring.SpringFormEncoder;
import org.springframework.web.multipart.MultipartFile;

public class MachineLearningClientImpl {
    private static final String URL = "http://localhost:8000/infer/";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PredictionDTO makeInference(MultipartFile image) {
        try {
            MachineLearningClient fileMachineLearningClient = Feign.builder().encoder(new SpringFormEncoder())
                    .target(MachineLearningClient.class, URL);
            Response response = fileMachineLearningClient.uploadFile(image);
            return MAPPER.readValue(response.body().toString(), PredictionDTO.class);
        } catch (Exception e) {
            throw new ApiException("Ocurrió un problema obteniendo la inferencia");
        }
    }
}
