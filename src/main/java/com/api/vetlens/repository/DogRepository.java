package com.api.vetlens.repository;

import com.api.vetlens.entity.Dog;
import com.api.vetlens.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Integer> {
    List<Dog> findAllByOwner(User owner);
    Optional<Dog> findById(Integer id);

    Optional<Dog> findByName(String name);
}
