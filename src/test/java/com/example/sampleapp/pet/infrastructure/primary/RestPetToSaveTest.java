package com.example.sampleapp.pet.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class RestPetToSaveTest {

  @Test
  void shouldBuildDomainCreateCommand() {
    UUID breedId = UUID.randomUUID();
    RestPetToSave payload = new RestPetToSave("umee", breedId);

    assertThat(payload.toDomainCreate().name().get()).isEqualTo("umee");
    assertThat(payload.toDomainCreate().breedId().get()).isEqualTo(breedId);
  }

  @Test
  void shouldBuildDomainName() {
    RestPetToSave payload = new RestPetToSave("umee", UUID.randomUUID());

    assertThat(payload.toDomainName().get()).isEqualTo("umee");
  }

  @Test
  void shouldBuildDomainBreedId() {
    UUID breedId = UUID.randomUUID();
    RestPetToSave payload = new RestPetToSave("umee", breedId);

    assertThat(payload.toDomainBreedId().get()).isEqualTo(breedId);
  }
}
