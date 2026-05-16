package com.example.sampleapp.breed.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import com.example.sampleapp.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class BreedNameTest {

  @Test
  void shouldKeepProvidedValue() {
    assertThat(new BreedName("cane corso").get()).isEqualTo("cane corso");
  }

  @Test
  void shouldTrimWhitespace() {
    assertThat(new BreedName("  labrador  ").get()).isEqualTo("labrador");
  }

  @Test
  void shouldRejectBlankName() {
    assertThatThrownBy(() -> new BreedName("  ")).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNullName() {
    assertThatThrownBy(() -> new BreedName(null)).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNameLongerThanHundredCharacters() {
    String tooLong = "x".repeat(101);

    assertThatThrownBy(() -> new BreedName(tooLong)).isInstanceOf(StringTooLongException.class);
  }
}
