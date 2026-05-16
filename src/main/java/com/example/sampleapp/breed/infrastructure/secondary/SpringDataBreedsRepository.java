package com.example.sampleapp.breed.infrastructure.secondary;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataBreedsRepository extends JpaRepository<BreedEntity, UUID> {
  boolean existsByName(String name);
}
