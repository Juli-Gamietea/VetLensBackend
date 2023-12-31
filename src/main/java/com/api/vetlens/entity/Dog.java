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
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30)
    private String name;
    @Column(name = "dog_breed", length = 30)
    private String dogBreed;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "is_castrated")
    private boolean isCastrated;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    @OneToMany(mappedBy = "dog", fetch = FetchType.EAGER)
    private List<Diagnosis> diagnoses;
    private String photoUrl;
    private boolean deleted;
}
