package com.api.vetlens.dto;

import com.api.vetlens.entity.Role;
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
    private String licenceNumber;
    private Role role;
}
