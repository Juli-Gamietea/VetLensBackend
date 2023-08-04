package com.api.vetlens.repository;

import com.api.vetlens.entity.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnamnesisRepository extends JpaRepository<Anamnesis, Integer> {
}
