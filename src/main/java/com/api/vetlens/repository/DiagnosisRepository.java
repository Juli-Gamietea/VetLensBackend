package com.api.vetlens.repository;

import com.api.vetlens.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
    List<Diagnosis> findAllByDog_Id(Integer dogId);
}
