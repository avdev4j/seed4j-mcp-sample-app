package com.example.sampleapp.breed.domain;

import com.example.sampleapp.shared.error.domain.Assert;

public record BreedToCreate(BreedName name) {
  public BreedToCreate {
    Assert.notNull("name", name);
  }
}
