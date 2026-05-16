package com.example.sampleapp.breed.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class BreedTest {

  @Test
  void shouldBuildFromIdAndName() {
    BreedId id = BreedId.newId();
    BreedName name = new BreedName("cane corso");

    Breed breed = new Breed(id, name);

    assertThat(breed.id()).isEqualTo(id);
    assertThat(breed.name()).isEqualTo(name);
  }

  @Test
  void shouldRenamePreservingId() {
    BreedId id = BreedId.newId();
    Breed breed = new Breed(id, new BreedName("cane corso"));

    Breed renamed = breed.rename(new BreedName("labrador"));

    assertThat(renamed.id()).isEqualTo(id);
    assertThat(renamed.name().get()).isEqualTo("labrador");
  }

  @Test
  void shouldRejectNullId() {
    assertThatThrownBy(() -> new Breed(null, new BreedName("x"))).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNullName() {
    assertThatThrownBy(() -> new Breed(BreedId.newId(), null)).isInstanceOf(MissingMandatoryValueException.class);
  }
}
