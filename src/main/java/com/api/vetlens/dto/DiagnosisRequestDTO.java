package com.api.vetlens.dto;

import com.api.vetlens.dto.questionary.QuestionDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRequestDTO {
    private List<QuestionDTO> questions;
    private DogRequestDTO dog;
    private MultipartFile image;
    private String username;
}
