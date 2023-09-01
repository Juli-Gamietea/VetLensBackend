package com.api.vetlens.rest;

import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Response;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class MachineLearningClientImpl {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PredictionDTO makeInference(MultipartFile image) {
        try {
            log.info("Llamando al servicio de Machine Learning para obtener la inferencia...");
            MachineLearningClient fileMachineLearningClient = Feign.builder().encoder(new SpringFormEncoder())
                    .target(MachineLearningClient.class, "http://localhost:8000/infer/");
            Response response = fileMachineLearningClient.makePrediction(image);
            log.info(response.body().toString());
            log.info("Inferencia obtenida");
            return MAPPER.readValue(response.body().toString(), PredictionDTO.class);
        } catch (Exception e) {
            throw new ApiException("Ocurrió un problema obteniendo la inferencia");
        }
    }
}
