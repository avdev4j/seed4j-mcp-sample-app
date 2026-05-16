package com.example.sampleapp.breed.domain;

import com.example.sampleapp.shared.error.domain.Assert;

public record Breed(BreedId id, BreedName name) {
  public Breed {
    Assert.notNull("id", id);
    Assert.notNull("name", name);
  }

  public Breed rename(BreedName newName) {
    return new Breed(id, newName);
  }
}
