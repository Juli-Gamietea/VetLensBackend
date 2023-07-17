package com.api.vetlens.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "El campo 'nombre de usuario' no puede estar vacío")
    private String username;
    @Email(message = "Introducir email válido")
    private String email;
    @NotBlank(message = "El campo 'nombre' no puede estar vacío")
    @Size(max = 30, message = "El nombre no puede contener mas de 30 caracteres")
    @JsonProperty("first_name")
    private String firstName;
    @NotBlank(message = "El campo 'apellido' no puede estar vacío")
    @Size(max = 30, message = "El apellido no puede contener mas de 30 caracteres")
    @JsonProperty("last_name")
    private String lastname;
    @NotBlank(message = "El campo 'numero de licencia' no puede estar vacío")
    @Size(max = 40, message = "El nro de licencia no puede contener mas de 40 caracteres")
    @JsonProperty("licence_number")
    private String licenceNumber;
    @NotNull(message = "El campo 'rol' no puede estar vacío")
    private String role;
}
