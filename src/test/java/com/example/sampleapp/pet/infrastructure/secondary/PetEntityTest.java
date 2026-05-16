package com.example.sampleapp.pet.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class PetEntityTest {

  @Test
  void shouldRoundTripBetweenDomainAndEntity() {
    PetId id = PetId.newId();
    BreedId breedId = BreedId.newId();
    Pet pet = new Pet(id, new PetName("umee"), breedId);

    PetEntity entity = PetEntity.from(pet);

    Pet roundTrip = entity.toDomain();
    assertThat(roundTrip.id()).isEqualTo(id);
    assertThat(roundTrip.name().get()).isEqualTo("umee");
    assertThat(roundTrip.breedId()).isEqualTo(breedId);
  }

  @Test
  void shouldExposeNoArgConstructorForJpa() {
    PetEntity entity = new PetEntity();

    assertThat(entity).isNotNull();
  }

  @Test
  void shouldUpdateNameAndBreed() {
    PetEntity entity = PetEntity.from(new Pet(PetId.newId(), new PetName("umee"), BreedId.newId()));
    UUID newBreedId = UUID.randomUUID();

    entity.setName("yuki");
    entity.setBreedId(newBreedId);

    Pet updated = entity.toDomain();
    assertThat(updated.name().get()).isEqualTo("yuki");
    assertThat(updated.breedId().get()).isEqualTo(newBreedId);
  }
}
