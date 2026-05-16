package com.example.sampleapp.pet.domain;

public final class UnknownPetException extends RuntimeException {

  private UnknownPetException(String message) {
    super(message);
  }

  public static UnknownPetException forId(PetId id) {
    return new UnknownPetException("No pet found for id: " + id.get());
  }
}
