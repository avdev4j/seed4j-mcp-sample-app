package com.example.sampleapp.breed.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class JpaBreedsTest {

  @Mock
  private SpringDataBreedsRepository repository;

  @InjectMocks
  private JpaBreeds breeds;

  @Test
  void shouldSaveNewBreed() {
    Breed breed = new Breed(BreedId.newId(), new BreedName("cane corso"));
    when(repository.findById(breed.id().get())).thenReturn(Optional.empty());
    when(repository.save(any(BreedEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Breed saved = breeds.save(breed);

    assertThat(saved.id()).isEqualTo(breed.id());
    assertThat(saved.name()).isEqualTo(breed.name());
  }

  @Test
  void shouldUpdateExistingBreed() {
    Breed breed = new Breed(BreedId.newId(), new BreedName("labrador"));
    BreedEntity existing = BreedEntity.from(new Breed(breed.id(), new BreedName("cane corso")));
    when(repository.findById(breed.id().get())).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);

    Breed saved = breeds.save(breed);

    assertThat(saved.name().get()).isEqualTo("labrador");
  }

  @Test
  void shouldFindById() {
    BreedId id = BreedId.newId();
    BreedEntity entity = BreedEntity.from(new Breed(id, new BreedName("cane corso")));
    when(repository.findById(id.get())).thenReturn(Optional.of(entity));

    assertThat(breeds.findById(id)).hasValueSatisfying(breed -> assertThat(breed.name().get()).isEqualTo("cane corso"));
  }

  @Test
  void shouldFindAll() {
    BreedEntity entity = BreedEntity.from(new Breed(BreedId.newId(), new BreedName("cane corso")));
    when(repository.findAll()).thenReturn(List.of(entity));

    assertThat(breeds.findAll()).hasSize(1);
  }

  @Test
  void shouldCheckExistenceByName() {
    when(repository.existsByName("cane corso")).thenReturn(true);

    assertThat(breeds.existsByName(new BreedName("cane corso"))).isTrue();
  }

  @Test
  void shouldDelete() {
    BreedId id = BreedId.newId();

    breeds.delete(id);

    verify(repository).deleteById(id.get());
  }
}
