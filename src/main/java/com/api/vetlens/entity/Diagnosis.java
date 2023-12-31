package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "diagnosis")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "dog_id", referencedColumnName = "id")
    private Dog dog;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "anamnesis_id", referencedColumnName = "id")
    private Anamnesis anamnesis;
    @OneToMany(mappedBy = "diagnosis", fetch = FetchType.EAGER)
    private List<DiagnosisValidation> validations;
}
