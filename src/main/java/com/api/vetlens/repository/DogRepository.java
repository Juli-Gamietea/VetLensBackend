package com.api.vetlens.repository;

import com.api.vetlens.entity.Dog;
import com.api.vetlens.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Integer> {
    List<Dog> findAllByOwner(User owner);
}
