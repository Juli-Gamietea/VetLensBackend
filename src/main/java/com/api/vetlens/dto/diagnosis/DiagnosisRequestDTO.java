package com.api.vetlens.dto.diagnosis;

import com.api.vetlens.dto.questionary.QuestionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRequestDTO {
    @NotNull(message = "El campo 'preguntas' no puede estar vacío")
    private List<QuestionDTO> questions;
    @NotNull(message = "El campo 'id del perro' no puede estar vacío")
    @JsonProperty("dog_id")
    private Integer dogId;
    @NotBlank(message = "El campo 'nombre de usuario' no puede estar vacío")
    private String username;
}
