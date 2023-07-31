package com.api.vetlens.service;

import com.api.vetlens.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class InferenceService {

    public MessageDTO makeInference(MultipartFile image) {
        return null;
    }

}
