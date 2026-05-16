package com.example.sampleapp.pet.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class PetTest {

  @Test
  void shouldBuildFromIdNameAndBreedId() {
    PetId id = PetId.newId();
    PetName name = new PetName("umee");
    BreedId breedId = BreedId.newId();

    Pet pet = new Pet(id, name, breedId);

    assertThat(pet.id()).isEqualTo(id);
    assertThat(pet.name()).isEqualTo(name);
    assertThat(pet.breedId()).isEqualTo(breedId);
  }

  @Test
  void shouldChangeNamePreservingIdAndBreed() {
    PetId id = PetId.newId();
    BreedId breedId = BreedId.newId();
    Pet pet = new Pet(id, new PetName("umee"), breedId);

    Pet renamed = pet.withName(new PetName("yuki"));

    assertThat(renamed.id()).isEqualTo(id);
    assertThat(renamed.breedId()).isEqualTo(breedId);
    assertThat(renamed.name().get()).isEqualTo("yuki");
  }

  @Test
  void shouldChangeBreedPreservingIdAndName() {
    PetId id = PetId.newId();
    PetName name = new PetName("umee");
    Pet pet = new Pet(id, name, BreedId.newId());

    BreedId newBreed = BreedId.newId();
    Pet moved = pet.withBreed(newBreed);

    assertThat(moved.id()).isEqualTo(id);
    assertThat(moved.name()).isEqualTo(name);
    assertThat(moved.breedId()).isEqualTo(newBreed);
  }

  @Test
  void shouldRejectNullId() {
    assertThatThrownBy(() -> new Pet(null, new PetName("x"), BreedId.newId())).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNullName() {
    assertThatThrownBy(() -> new Pet(PetId.newId(), null, BreedId.newId())).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNullBreedId() {
    assertThatThrownBy(() -> new Pet(PetId.newId(), new PetName("x"), null)).isInstanceOf(MissingMandatoryValueException.class);
  }
}
