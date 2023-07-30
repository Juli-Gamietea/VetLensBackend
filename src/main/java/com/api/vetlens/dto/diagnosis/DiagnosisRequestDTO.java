package com.api.vetlens.dto.diagnosis;

import com.api.vetlens.dto.questionary.QuestionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRequestDTO {
    private List<QuestionDTO> questions;
    @JsonProperty("dog_id")
    private Integer dogId;
    private String username;
}
