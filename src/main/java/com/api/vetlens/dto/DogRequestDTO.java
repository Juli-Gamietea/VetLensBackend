package com.api.vetlens.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogRequestDTO {
    @NotBlank(message = "El campo 'nombre' no puede estar vacío")
    private String name;
    @NotBlank(message = "El campo 'raza' no puede estar vacío")
    @JsonProperty("dog_breed")
    private String dogBreed;
    @Past(message = "La fecha debe ser menor a la actual")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    @NotBlank(message = "El campo 'dueño' no puede estar vacío")
    @JsonProperty("owner_username")
    private String ownerUsername;
    @NotBlank(message = "El campo 'sexo' no puede estar vacío")
    private String sex;
    @JsonProperty("is_castrated")
    private boolean isCastrated;
}
