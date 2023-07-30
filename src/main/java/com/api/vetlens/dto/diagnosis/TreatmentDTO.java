package com.api.vetlens.dto.diagnosis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDTO {
    private Integer id;
    private String name;
    private String summary;
    private String source;
}
