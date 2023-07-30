package com.api.vetlens.service;

import com.api.vetlens.entity.Inference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InferenceService {

    public List<Inference> makeInference(MultipartFile image){
        return null;
    }
}
