package com.example.sampleapp.breed.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class BreedIdTest {

  @Test
  void shouldGenerateUniqueIds() {
    assertThat(BreedId.newId().get()).isNotEqualTo(BreedId.newId().get());
  }

  @Test
  void shouldRejectNullUuid() {
    assertThatThrownBy(() -> new BreedId(null)).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldParseFromString() {
    UUID raw = UUID.randomUUID();

    assertThat(BreedId.from(raw.toString()).get()).isEqualTo(raw);
  }
}
