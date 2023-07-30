package com.api.vetlens.dto.user;

import com.api.vetlens.entity.Role;
import com.api.vetlens.entity.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    @JsonProperty("licence_number")
    private String licenceNumber;
    private Role role;
}
