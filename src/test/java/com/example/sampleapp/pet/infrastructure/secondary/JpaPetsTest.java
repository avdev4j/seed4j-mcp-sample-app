package com.example.sampleapp.pet.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class JpaPetsTest {

  @Mock
  private SpringDataPetsRepository repository;

  @InjectMocks
  private JpaPets pets;

  private Pet samplePet() {
    return new Pet(PetId.newId(), new PetName("umee"), BreedId.newId());
  }

  @Test
  void shouldSaveNewPet() {
    Pet pet = samplePet();
    when(repository.findById(pet.id().get())).thenReturn(Optional.empty());
    when(repository.save(any(PetEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Pet saved = pets.save(pet);

    assertThat(saved.id()).isEqualTo(pet.id());
    assertThat(saved.name()).isEqualTo(pet.name());
    assertThat(saved.breedId()).isEqualTo(pet.breedId());
  }

  @Test
  void shouldUpdateExistingPet() {
    PetId id = PetId.newId();
    BreedId originalBreed = BreedId.newId();
    BreedId newBreed = BreedId.newId();
    PetEntity existing = PetEntity.from(new Pet(id, new PetName("umee"), originalBreed));
    Pet update = new Pet(id, new PetName("yuki"), newBreed);
    when(repository.findById(id.get())).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);

    Pet saved = pets.save(update);

    assertThat(saved.name().get()).isEqualTo("yuki");
    assertThat(saved.breedId()).isEqualTo(newBreed);
  }

  @Test
  void shouldFindById() {
    Pet pet = samplePet();
    when(repository.findById(pet.id().get())).thenReturn(Optional.of(PetEntity.from(pet)));

    assertThat(pets.findById(pet.id())).hasValueSatisfying(found -> assertThat(found.name()).isEqualTo(pet.name()));
  }

  @Test
  void shouldFindAll() {
    Pet pet = samplePet();
    when(repository.findAll()).thenReturn(List.of(PetEntity.from(pet)));

    assertThat(pets.findAll()).hasSize(1);
  }

  @Test
  void shouldDelete() {
    PetId id = PetId.newId();

    pets.delete(id);

    verify(repository).deleteById(id.get());
  }
}
