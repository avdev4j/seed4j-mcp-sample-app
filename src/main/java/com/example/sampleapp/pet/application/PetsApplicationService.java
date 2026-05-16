package com.example.sampleapp.pet.application;

import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.Breeds;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import com.example.sampleapp.pet.domain.PetToCreate;
import com.example.sampleapp.pet.domain.Pets;
import com.example.sampleapp.pet.domain.UnknownPetException;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetsApplicationService {

  private final Pets pets;
  private final Breeds breeds;

  public PetsApplicationService(Pets pets, Breeds breeds) {
    this.pets = pets;
    this.breeds = breeds;
  }

  @Transactional(readOnly = true)
  public Collection<Pet> findAll() {
    return pets.findAll();
  }

  @Transactional(readOnly = true)
  public Pet get(PetId id) {
    return pets.findById(id).orElseThrow(() -> UnknownPetException.forId(id));
  }

  @Transactional
  public Pet create(PetToCreate toCreate) {
    ensureBreedExists(toCreate.breedId());
    return pets.save(new Pet(PetId.newId(), toCreate.name(), toCreate.breedId()));
  }

  @Transactional
  public Pet update(PetId id, PetName name, BreedId breedId) {
    Pet existing = get(id);
    if (!existing.breedId().equals(breedId)) {
      ensureBreedExists(breedId);
    }
    return pets.save(existing.withName(name).withBreed(breedId));
  }

  @Transactional
  public void delete(PetId id) {
    pets.delete(id);
  }

  private void ensureBreedExists(BreedId breedId) {
    if (breeds.findById(breedId).isEmpty()) {
      throw UnknownBreedException.forId(breedId);
    }
  }
}
