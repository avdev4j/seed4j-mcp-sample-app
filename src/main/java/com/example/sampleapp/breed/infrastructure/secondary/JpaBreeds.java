package com.example.sampleapp.breed.infrastructure.secondary;

import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.Breeds;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaBreeds implements Breeds {

  private final SpringDataBreedsRepository repository;

  JpaBreeds(SpringDataBreedsRepository repository) {
    this.repository = repository;
  }

  @Override
  public Breed save(Breed breed) {
    BreedEntity entity = repository
      .findById(breed.id().get())
      .map(existing -> {
        existing.setName(breed.name().get());
        return existing;
      })
      .orElseGet(() -> BreedEntity.from(breed));
    return repository.save(entity).toDomain();
  }

  @Override
  public Optional<Breed> findById(BreedId id) {
    return repository.findById(id.get()).map(BreedEntity::toDomain);
  }

  @Override
  public Collection<Breed> findAll() {
    return List.copyOf(repository.findAll().stream().map(BreedEntity::toDomain).toList());
  }

  @Override
  public boolean existsByName(BreedName name) {
    return repository.existsByName(name.get());
  }

  @Override
  public void delete(BreedId id) {
    repository.deleteById(id.get());
  }
}
