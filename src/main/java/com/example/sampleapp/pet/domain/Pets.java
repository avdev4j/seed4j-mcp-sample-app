package com.example.sampleapp.pet.domain;

import java.util.Collection;
import java.util.Optional;

public interface Pets {
  Pet save(Pet pet);

  Optional<Pet> findById(PetId id);

  Collection<Pet> findAll();

  void delete(PetId id);
}
