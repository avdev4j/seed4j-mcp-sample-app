package com.example.sampleapp.breed.application;

import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.BreedNameAlreadyUsedException;
import com.example.sampleapp.breed.domain.BreedToCreate;
import com.example.sampleapp.breed.domain.Breeds;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BreedsApplicationService {

  private final Breeds breeds;

  public BreedsApplicationService(Breeds breeds) {
    this.breeds = breeds;
  }

  @Transactional(readOnly = true)
  public Collection<Breed> findAll() {
    return breeds.findAll();
  }

  @Transactional(readOnly = true)
  public Breed get(BreedId id) {
    return breeds.findById(id).orElseThrow(() -> UnknownBreedException.forId(id));
  }

  @Transactional
  public Breed create(BreedToCreate toCreate) {
    ensureNameIsAvailable(toCreate.name());
    return breeds.save(new Breed(BreedId.newId(), toCreate.name()));
  }

  @Transactional
  public Breed rename(BreedId id, BreedName newName) {
    Breed existing = get(id);
    if (!existing.name().equals(newName)) {
      ensureNameIsAvailable(newName);
    }
    return breeds.save(existing.rename(newName));
  }

  @Transactional
  public void delete(BreedId id) {
    breeds.delete(id);
  }

  private void ensureNameIsAvailable(BreedName name) {
    if (breeds.existsByName(name)) {
      throw new BreedNameAlreadyUsedException(name);
    }
  }
}
