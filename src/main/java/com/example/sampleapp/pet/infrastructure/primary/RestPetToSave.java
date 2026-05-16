package com.example.sampleapp.pet.infrastructure.primary;

import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.pet.domain.PetName;
import com.example.sampleapp.pet.domain.PetToCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

record RestPetToSave(@NotBlank String name, @NotNull UUID breedId) {
  PetToCreate toDomainCreate() {
    return new PetToCreate(new PetName(name), new BreedId(breedId));
  }

  PetName toDomainName() {
    return new PetName(name);
  }

  BreedId toDomainBreedId() {
    return new BreedId(breedId);
  }
}
