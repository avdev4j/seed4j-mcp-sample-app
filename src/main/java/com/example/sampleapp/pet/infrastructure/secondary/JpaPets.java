package com.example.sampleapp.pet.infrastructure.secondary;

import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.Pets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaPets implements Pets {

  private final SpringDataPetsRepository repository;

  JpaPets(SpringDataPetsRepository repository) {
    this.repository = repository;
  }

  @Override
  public Pet save(Pet pet) {
    PetEntity entity = repository
      .findById(pet.id().get())
      .map(existing -> {
        existing.setName(pet.name().get());
        existing.setBreedId(pet.breedId().get());
        return existing;
      })
      .orElseGet(() -> PetEntity.from(pet));
    return repository.save(entity).toDomain();
  }

  @Override
  public Optional<Pet> findById(PetId id) {
    return repository.findById(id.get()).map(PetEntity::toDomain);
  }

  @Override
  public Collection<Pet> findAll() {
    return List.copyOf(repository.findAll().stream().map(PetEntity::toDomain).toList());
  }

  @Override
  public void delete(PetId id) {
    repository.deleteById(id.get());
  }
}
