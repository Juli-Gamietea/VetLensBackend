package com.api.vetlens.dto.diagnosis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnamnesisDTO {
    private Integer id;
    @JsonProperty("questionary_id")
    private String questionaryId;
    private List<InferenceDTO> inferences;
    private String result;
}
