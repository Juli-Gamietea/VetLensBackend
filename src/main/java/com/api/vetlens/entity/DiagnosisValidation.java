package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diagnosis_validation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DiagnosisValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "vet_id", referencedColumnName = "id")
    private User vet;
    @Enumerated(EnumType.STRING)
    private Value value;
    @ManyToOne
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;
    private String notes;
    private String disease;
}
