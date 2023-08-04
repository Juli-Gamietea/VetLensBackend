package com.api.vetlens.repository;

import com.api.vetlens.entity.Inference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InferenceRepository extends JpaRepository<Inference, Integer> {
}
