package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "qas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String question;
    private String answer;
    @ManyToOne
    @JoinColumn(name = "anamnesis_id", referencedColumnName = "id")
    private Anamnesis anamnesis;
}
