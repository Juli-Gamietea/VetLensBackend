package com.api.vetlens.entity.questionary;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Weight {
    private Float dermatophytosis;
    private Float pyotraumaticDermatitis;
    private Float myiasis;
    private Float other;
}
