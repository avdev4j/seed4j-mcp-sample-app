package com.example.sampleapp.breed.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class RestBreedToSaveTest {

  @Test
  void shouldBuildDomainCreateCommand() {
    RestBreedToSave payload = new RestBreedToSave("cane corso");

    assertThat(payload.toDomainCreate().name().get()).isEqualTo("cane corso");
  }

  @Test
  void shouldBuildDomainName() {
    RestBreedToSave payload = new RestBreedToSave("labrador");

    assertThat(payload.toDomainName().get()).isEqualTo("labrador");
  }
}
