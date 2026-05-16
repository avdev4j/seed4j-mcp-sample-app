package com.example.sampleapp.pet.infrastructure.secondary;

import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "pet")
class PetEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "breed_id", nullable = false)
  private UUID breedId;

  protected PetEntity() {}

  private PetEntity(UUID id, String name, UUID breedId) {
    this.id = id;
    this.name = name;
    this.breedId = breedId;
  }

  static PetEntity from(Pet pet) {
    return new PetEntity(pet.id().get(), pet.name().get(), pet.breedId().get());
  }

  Pet toDomain() {
    return new Pet(new PetId(id), new PetName(name), new BreedId(breedId));
  }

  void setName(String name) {
    this.name = name;
  }

  void setBreedId(UUID breedId) {
    this.breedId = breedId;
  }
}
