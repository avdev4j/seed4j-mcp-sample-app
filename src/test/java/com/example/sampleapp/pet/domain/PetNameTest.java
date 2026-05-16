package com.example.sampleapp.pet.domain;

import static org.assertj.core.api.Assertions.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.shared.error.domain.MissingMandatoryValueException;
import com.example.sampleapp.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class PetNameTest {

  @Test
  void shouldKeepProvidedValue() {
    assertThat(new PetName("umee").get()).isEqualTo("umee");
  }

  @Test
  void shouldTrimWhitespace() {
    assertThat(new PetName("  umee  ").get()).isEqualTo("umee");
  }

  @Test
  void shouldRejectBlankName() {
    assertThatThrownBy(() -> new PetName("  ")).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNullName() {
    assertThatThrownBy(() -> new PetName(null)).isInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldRejectNameLongerThanHundredCharacters() {
    String tooLong = "x".repeat(101);

    assertThatThrownBy(() -> new PetName(tooLong)).isInstanceOf(StringTooLongException.class);
  }
}
