package com.example.sampleapp.breed.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import org.junit.jupiter.api.Test;

@UnitTest
class BreedEntityTest {

  @Test
  void shouldRoundTripBetweenDomainAndEntity() {
    BreedId id = BreedId.newId();
    Breed breed = new Breed(id, new BreedName("cane corso"));

    BreedEntity entity = BreedEntity.from(breed);

    assertThat(entity.getId()).isEqualTo(id.get());
    Breed roundTrip = entity.toDomain();
    assertThat(roundTrip.id()).isEqualTo(id);
    assertThat(roundTrip.name().get()).isEqualTo("cane corso");
  }

  @Test
  void shouldExposeNoArgConstructorForJpa() {
    BreedEntity entity = new BreedEntity();

    assertThat(entity.getId()).isNull();
  }

  @Test
  void shouldUpdateName() {
    BreedEntity entity = BreedEntity.from(new Breed(BreedId.newId(), new BreedName("cane corso")));

    entity.setName("labrador");

    assertThat(entity.toDomain().name().get()).isEqualTo("labrador");
  }
}
