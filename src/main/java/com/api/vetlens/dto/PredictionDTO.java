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
    private Float pyotraumaticDermatitis;
    @JsonProperty("dermatofitosis")
    private Float dermatophytosis;
    @JsonProperty("miasis")
    private Float myasis;
    @JsonProperty("otras")
    private Float others;
}
