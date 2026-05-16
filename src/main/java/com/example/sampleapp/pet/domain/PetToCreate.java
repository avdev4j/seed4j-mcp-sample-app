package com.example.sampleapp.pet.domain;

import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.shared.error.domain.Assert;

public record PetToCreate(PetName name, BreedId breedId) {
  public PetToCreate {
    Assert.notNull("name", name);
    Assert.notNull("breedId", breedId);
  }
}
