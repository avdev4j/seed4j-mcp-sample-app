package com.example.sampleapp.pet.domain;

import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.shared.error.domain.Assert;

public record Pet(PetId id, PetName name, BreedId breedId) {
  public Pet {
    Assert.notNull("id", id);
    Assert.notNull("name", name);
    Assert.notNull("breedId", breedId);
  }

  public Pet withName(PetName newName) {
    return new Pet(id, newName, breedId);
  }

  public Pet withBreed(BreedId newBreedId) {
    return new Pet(id, name, newBreedId);
  }
}
