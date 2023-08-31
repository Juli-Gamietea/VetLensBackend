package com.api.vetlens.dto.diagnosis;

import com.api.vetlens.dto.user.UserResponseDTO;
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
    private UserResponseDTO vet;
    private Value value;
    private DiagnosisResponseDTO diagnosis;
    private String notes;
    private String disease;
}
