package com.api.vetlens.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredictionDTO {
    @JsonProperty("dermatitis_piotraumatica")
    private Float dermatitisPiotraumatica;
    private Float dermatofitosis;
    private Float miasis;
    private Float otras;
}
