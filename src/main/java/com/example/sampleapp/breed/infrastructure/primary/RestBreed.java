package com.example.sampleapp.breed.infrastructure.primary;

import com.example.sampleapp.breed.domain.Breed;
import java.util.UUID;

record RestBreed(UUID id, String name) {
  static RestBreed from(Breed breed) {
    return new RestBreed(breed.id().get(), breed.name().get());
  }
}
