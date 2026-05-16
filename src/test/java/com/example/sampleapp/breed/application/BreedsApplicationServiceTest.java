package com.example.sampleapp.breed.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.BreedNameAlreadyUsedException;
import com.example.sampleapp.breed.domain.BreedToCreate;
import com.example.sampleapp.breed.domain.Breeds;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BreedsApplicationServiceTest {

  @Mock
  private Breeds breeds;

  @InjectMocks
  private BreedsApplicationService service;

  @Test
  void shouldCreateBreedWhenNameIsFree() {
    BreedName name = new BreedName("cane corso");
    when(breeds.existsByName(name)).thenReturn(false);
    when(breeds.save(any(Breed.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Breed created = service.create(new BreedToCreate(name));

    assertThat(created.name()).isEqualTo(name);
    verify(breeds).save(any(Breed.class));
  }

  @Test
  void shouldRejectCreateWhenNameIsAlreadyUsed() {
    BreedName name = new BreedName("cane corso");
    when(breeds.existsByName(name)).thenReturn(true);

    assertThatThrownBy(() -> service.create(new BreedToCreate(name))).isInstanceOf(BreedNameAlreadyUsedException.class);
    verify(breeds, never()).save(any());
  }

  @Test
  void shouldRenameBreed() {
    BreedId id = BreedId.newId();
    Breed existing = new Breed(id, new BreedName("cane corso"));
    BreedName newName = new BreedName("labrador");
    when(breeds.findById(id)).thenReturn(Optional.of(existing));
    when(breeds.existsByName(newName)).thenReturn(false);
    when(breeds.save(any(Breed.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Breed renamed = service.rename(id, newName);

    assertThat(renamed.id()).isEqualTo(id);
    assertThat(renamed.name()).isEqualTo(newName);
  }

  @Test
  void shouldAllowRenameToSameName() {
    BreedId id = BreedId.newId();
    BreedName name = new BreedName("cane corso");
    Breed existing = new Breed(id, name);
    when(breeds.findById(id)).thenReturn(Optional.of(existing));
    when(breeds.save(any(Breed.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Breed result = service.rename(id, name);

    assertThat(result.name()).isEqualTo(name);
    verify(breeds, never()).existsByName(any());
  }

  @Test
  void shouldRejectRenameWhenNewNameIsAlreadyUsed() {
    BreedId id = BreedId.newId();
    Breed existing = new Breed(id, new BreedName("cane corso"));
    BreedName newName = new BreedName("labrador");
    when(breeds.findById(id)).thenReturn(Optional.of(existing));
    when(breeds.existsByName(newName)).thenReturn(true);

    assertThatThrownBy(() -> service.rename(id, newName)).isInstanceOf(BreedNameAlreadyUsedException.class);
    verify(breeds, never()).save(any());
  }

  @Test
  void shouldListAllBreeds() {
    Breed breed = new Breed(BreedId.newId(), new BreedName("cane corso"));
    when(breeds.findAll()).thenReturn(java.util.List.of(breed));

    assertThat(service.findAll()).containsExactly(breed);
  }

  @Test
  void shouldGetExistingBreed() {
    BreedId id = BreedId.newId();
    Breed breed = new Breed(id, new BreedName("cane corso"));
    when(breeds.findById(id)).thenReturn(Optional.of(breed));

    assertThat(service.get(id)).isEqualTo(breed);
  }

  @Test
  void shouldThrowWhenGettingUnknownBreed() {
    BreedId id = BreedId.newId();
    when(breeds.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(id)).isInstanceOf(UnknownBreedException.class);
  }

  @Test
  void shouldDelegateDeleteToPort() {
    BreedId id = BreedId.newId();

    service.delete(id);

    verify(breeds).delete(id);
  }
}
