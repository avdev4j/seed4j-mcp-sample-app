package com.example.sampleapp.breed.domain;

public final class UnknownBreedException extends RuntimeException {

  private UnknownBreedException(String message) {
    super(message);
  }

  public static UnknownBreedException forId(BreedId id) {
    return new UnknownBreedException("No breed found for id: " + id.get());
  }
}
