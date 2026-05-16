package com.example.sampleapp.pet.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import org.junit.jupiter.api.Test;

@UnitTest
class RestPetTest {

  @Test
  void shouldMapFromDomain() {
    PetId id = PetId.newId();
    BreedId breedId = BreedId.newId();
    Pet pet = new Pet(id, new PetName("umee"), breedId);

    RestPet rest = RestPet.from(pet);

    assertThat(rest.id()).isEqualTo(id.get());
    assertThat(rest.name()).isEqualTo("umee");
    assertThat(rest.breedId()).isEqualTo(breedId.get());
  }
}
