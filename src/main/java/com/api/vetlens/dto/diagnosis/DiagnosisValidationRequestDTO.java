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
public class DiagnosisValidationRequestDTO {
    private Integer id;
    private String value;
    private String notes;
    private String disease;
}
