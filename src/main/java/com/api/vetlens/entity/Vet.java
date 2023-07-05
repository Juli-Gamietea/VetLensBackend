package com.api.vetlens.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vet extends User {
    @Column(name = "licence_number", length = 40)
    private String licenceNumber;
}
