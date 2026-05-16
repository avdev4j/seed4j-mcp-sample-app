package com.example.sampleapp.breed.domain;

import java.util.Collection;
import java.util.Optional;

public interface Breeds {
  Breed save(Breed breed);

  Optional<Breed> findById(BreedId id);

  Collection<Breed> findAll();

  boolean existsByName(BreedName name);

  void delete(BreedId id);
}
