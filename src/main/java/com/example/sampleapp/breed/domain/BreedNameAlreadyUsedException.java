package com.example.sampleapp.breed.domain;

public final class BreedNameAlreadyUsedException extends RuntimeException {

  public BreedNameAlreadyUsedException(BreedName name) {
    super("Breed name already used: " + name.get());
  }
}
