package com.api.vetlens.dto.questionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WeightDTO {
    private Float dermatophytosis;
    @JsonProperty("pyotraumatic_dermatitis")
    private Float pyotraumaticDermatitis;
    private Float myiasis;
    private Float other;
}
