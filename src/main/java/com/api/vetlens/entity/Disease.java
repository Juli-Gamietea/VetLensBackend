package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "diseases")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String summary;
    @OneToMany(mappedBy = "disease", fetch = FetchType.EAGER)
    private List<Treatment> treatments;
    @OneToOne(mappedBy = "disease")
    private Inference inference;
}
