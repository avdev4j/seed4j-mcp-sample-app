package com.example.sampleapp.pet.domain;

import com.example.sampleapp.shared.error.domain.Assert;

public record PetName(String get) {
  public PetName {
    Assert.field("name", get).notBlank().maxLength(100);
    get = get.trim();
  }
}
