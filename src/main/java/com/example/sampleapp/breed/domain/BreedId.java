package com.example.sampleapp.breed.domain;

import com.example.sampleapp.shared.error.domain.Assert;
import java.util.UUID;

public record BreedId(UUID get) {
  public BreedId {
    Assert.notNull("breedId", get);
  }

  public static BreedId newId() {
    return new BreedId(UUID.randomUUID());
  }

  public static BreedId from(String raw) {
    Assert.notBlank("breedId", raw);
    return new BreedId(UUID.fromString(raw));
  }
}
