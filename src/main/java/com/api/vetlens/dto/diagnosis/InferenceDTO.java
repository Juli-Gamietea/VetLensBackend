package com.api.vetlens.dto.diagnosis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InferenceDTO {
    private Integer id;
    private DiseaseDTO disease;
    private String probability;
}
