package com.api.vetlens.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "dogs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30)
    private String name;
    @Column(name = "dog_breed", length = 30)
    private String dogBreed;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    @OneToMany(mappedBy = "dog", fetch = FetchType.EAGER)
    private List<Diagnosis> diagnoses;
}
