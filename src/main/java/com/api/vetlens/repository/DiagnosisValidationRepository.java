package com.api.vetlens.repository;

import com.api.vetlens.entity.DiagnosisValidation;
import com.api.vetlens.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiagnosisValidationRepository extends JpaRepository<DiagnosisValidation, Integer> {
    List<DiagnosisValidation> findAllByVet_Id(Integer userId);
    List<DiagnosisValidation> findAllByVet_IdAndValue(Integer userId, Value value);
    Optional<DiagnosisValidation> findByVet_IdAndDiagnosis_Id(Integer userId, Integer diagnosisId);
    List<DiagnosisValidation> findAllByDiagnosis_Id(Integer diagnosisId);
}
