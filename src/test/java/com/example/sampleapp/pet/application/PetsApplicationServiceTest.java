package com.example.sampleapp.pet.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.Breeds;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import com.example.sampleapp.pet.domain.PetToCreate;
import com.example.sampleapp.pet.domain.Pets;
import com.example.sampleapp.pet.domain.UnknownPetException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class PetsApplicationServiceTest {

  @Mock
  private Pets pets;

  @Mock
  private Breeds breeds;

  @InjectMocks
  private PetsApplicationService service;

  @Test
  void shouldCreatePetWhenBreedExists() {
    BreedId breedId = BreedId.newId();
    PetName name = new PetName("umee");
    when(breeds.findById(breedId)).thenReturn(Optional.of(new Breed(breedId, new BreedName("cane corso"))));
    when(pets.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Pet created = service.create(new PetToCreate(name, breedId));

    assertThat(created.name()).isEqualTo(name);
    assertThat(created.breedId()).isEqualTo(breedId);
    verify(pets).save(any(Pet.class));
  }

  @Test
  void shouldRejectCreateWhenBreedIsUnknown() {
    BreedId breedId = BreedId.newId();
    when(breeds.findById(breedId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.create(new PetToCreate(new PetName("umee"), breedId))).isInstanceOf(UnknownBreedException.class);
    verify(pets, never()).save(any());
  }

  @Test
  void shouldUpdatePetNameAndBreed() {
    PetId id = PetId.newId();
    BreedId originalBreed = BreedId.newId();
    BreedId newBreed = BreedId.newId();
    Pet existing = new Pet(id, new PetName("umee"), originalBreed);
    when(pets.findById(id)).thenReturn(Optional.of(existing));
    when(breeds.findById(newBreed)).thenReturn(Optional.of(new Breed(newBreed, new BreedName("labrador"))));
    when(pets.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Pet updated = service.update(id, new PetName("yuki"), newBreed);

    assertThat(updated.id()).isEqualTo(id);
    assertThat(updated.name().get()).isEqualTo("yuki");
    assertThat(updated.breedId()).isEqualTo(newBreed);
  }

  @Test
  void shouldSkipBreedCheckWhenBreedIsUnchanged() {
    PetId id = PetId.newId();
    BreedId breedId = BreedId.newId();
    Pet existing = new Pet(id, new PetName("umee"), breedId);
    when(pets.findById(id)).thenReturn(Optional.of(existing));
    when(pets.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

    service.update(id, new PetName("yuki"), breedId);

    verify(breeds, never()).findById(any());
  }

  @Test
  void shouldRejectUpdateWhenNewBreedIsUnknown() {
    PetId id = PetId.newId();
    Pet existing = new Pet(id, new PetName("umee"), BreedId.newId());
    BreedId newBreed = BreedId.newId();
    when(pets.findById(id)).thenReturn(Optional.of(existing));
    when(breeds.findById(newBreed)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.update(id, new PetName("umee"), newBreed)).isInstanceOf(UnknownBreedException.class);
    verify(pets, never()).save(any());
  }

  @Test
  void shouldListAllPets() {
    Pet pet = new Pet(PetId.newId(), new PetName("umee"), BreedId.newId());
    when(pets.findAll()).thenReturn(java.util.List.of(pet));

    assertThat(service.findAll()).containsExactly(pet);
  }

  @Test
  void shouldGetExistingPet() {
    PetId id = PetId.newId();
    Pet pet = new Pet(id, new PetName("umee"), BreedId.newId());
    when(pets.findById(id)).thenReturn(Optional.of(pet));

    assertThat(service.get(id)).isEqualTo(pet);
  }

  @Test
  void shouldThrowWhenGettingUnknownPet() {
    PetId id = PetId.newId();
    when(pets.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(id)).isInstanceOf(UnknownPetException.class);
  }

  @Test
  void shouldDelegateDeleteToPort() {
    PetId id = PetId.newId();

    service.delete(id);

    verify(pets).delete(id);
  }
}
