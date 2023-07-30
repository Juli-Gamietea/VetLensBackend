package com.api.vetlens.dto.diagnosis;


import com.api.vetlens.dto.dog.DogResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResponseDTO {
    private Integer id;
    private LocalDate date;
    private DogResponseDTO dog;
    @JsonProperty("image_url")
    private String imageUrl;
    private AnamnesisDTO anamnesis;
}
