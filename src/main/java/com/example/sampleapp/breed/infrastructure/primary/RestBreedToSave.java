package com.example.sampleapp.breed.infrastructure.primary;

import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.BreedToCreate;
import jakarta.validation.constraints.NotBlank;

record RestBreedToSave(@NotBlank String name) {
  BreedToCreate toDomainCreate() {
    return new BreedToCreate(new BreedName(name));
  }

  BreedName toDomainName() {
    return new BreedName(name);
  }
}
