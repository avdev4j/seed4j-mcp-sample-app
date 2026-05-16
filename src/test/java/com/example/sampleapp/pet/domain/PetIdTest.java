package com.example.sampleapp.pet.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class PetIdTest {

  @Test
  void shouldGenerateUniqueIds() {
    assertThat(PetId.newId().get()).isNotEqualTo(PetId.newId().get());
  }

  @Test
  void shouldRejectNullUuid() {
    assertThatThrownBy(() -> new PetId(null)).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldParseFromString() {
    UUID raw = UUID.randomUUID();

    assertThat(PetId.from(raw.toString()).get()).isEqualTo(raw);
  }
}
