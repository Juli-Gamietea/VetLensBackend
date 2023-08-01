package com.api.vetlens.dto.diagnosis;

import com.api.vetlens.entity.Diagnosis;
import com.api.vetlens.entity.User;
import com.api.vetlens.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisValidationDTO {
    private Integer id;
    private User vet;
    private Value value;
    private Diagnosis diagnosis;
    private String notes;
    private String disease;
}
