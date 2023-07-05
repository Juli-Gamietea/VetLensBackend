package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "anamnesis")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Anamnesis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "anamnesis", fetch = FetchType.EAGER)
    private List<QA> qas;
    @OneToMany(mappedBy = "anamnesis", fetch = FetchType.EAGER)
    private List<Inference> inferences;
    @OneToOne(mappedBy = "anamnesis")
    private Diagnosis diagnosis;
}
