package com.example.sampleapp.breed.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import org.junit.jupiter.api.Test;

@UnitTest
class RestBreedTest {

  @Test
  void shouldMapFromDomain() {
    BreedId id = BreedId.newId();
    Breed breed = new Breed(id, new BreedName("cane corso"));

    RestBreed rest = RestBreed.from(breed);

    assertThat(rest.id()).isEqualTo(id.get());
    assertThat(rest.name()).isEqualTo("cane corso");
  }
}
