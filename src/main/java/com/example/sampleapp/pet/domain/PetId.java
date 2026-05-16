package com.example.sampleapp.pet.domain;

import com.example.sampleapp.shared.error.domain.Assert;
import java.util.UUID;

public record PetId(UUID get) {
  public PetId {
    Assert.notNull("petId", get);
  }

  public static PetId newId() {
    return new PetId(UUID.randomUUID());
  }

  public static PetId from(String raw) {
    Assert.notBlank("petId", raw);
    return new PetId(UUID.fromString(raw));
  }
}
