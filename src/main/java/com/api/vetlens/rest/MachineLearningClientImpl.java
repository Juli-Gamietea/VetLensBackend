package com.api.vetlens.rest;

import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Response;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class MachineLearningClientImpl {
    @Value("${python.url}")
    private String url;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PredictionDTO makeInference(MultipartFile image) {
        try {
            MachineLearningClient fileMachineLearningClient = Feign.builder().encoder(new SpringFormEncoder())
                    .target(MachineLearningClient.class, url);
            Response response = fileMachineLearningClient.makePrediction(image);
            System.out.println(response.body().toString());
            return MAPPER.readValue(response.body().toString(), PredictionDTO.class);
        } catch (Exception e) {
            throw new ApiException("Ocurrió un problema obteniendo la inferencia");
        }
    }
}
