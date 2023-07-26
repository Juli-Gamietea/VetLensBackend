package com.api.vetlens.dto;

import com.api.vetlens.entity.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DogResponseDTO {
    private Integer id;
    private String name;
    @JsonProperty("dog_breed")
    private String dogBreed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    @JsonProperty("owner_username")
    private String ownerUsername;
    private Sex sex;
    @JsonProperty("is_castrated")
    private boolean isCastrated;
}
