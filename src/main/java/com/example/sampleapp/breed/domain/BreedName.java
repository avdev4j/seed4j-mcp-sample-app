package com.example.sampleapp.breed.domain;

import com.example.sampleapp.shared.error.domain.Assert;

public record BreedName(String get) {
  public BreedName {
    Assert.field("name", get).notBlank().maxLength(100);
    get = get.trim();
  }
}
