package com.example.sampleapp.pet.infrastructure.primary;

import com.example.sampleapp.pet.domain.Pet;
import java.util.UUID;

record RestPet(UUID id, String name, UUID breedId) {
  static RestPet from(Pet pet) {
    return new RestPet(pet.id().get(), pet.name().get(), pet.breedId().get());
  }
}
