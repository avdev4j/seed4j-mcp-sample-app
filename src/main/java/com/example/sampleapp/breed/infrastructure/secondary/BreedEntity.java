package com.example.sampleapp.breed.infrastructure.secondary;

import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "breed")
class BreedEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name", nullable = false, unique = true, length = 100)
  private String name;

  protected BreedEntity() {}

  private BreedEntity(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  static BreedEntity from(Breed breed) {
    return new BreedEntity(breed.id().get(), breed.name().get());
  }

  Breed toDomain() {
    return new Breed(new BreedId(id), new BreedName(name));
  }

  UUID getId() {
    return id;
  }

  void setName(String name) {
    this.name = name;
  }
}
