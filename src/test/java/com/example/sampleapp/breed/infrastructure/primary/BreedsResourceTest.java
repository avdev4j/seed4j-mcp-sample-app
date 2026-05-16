package com.example.sampleapp.breed.infrastructure.primary;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.application.BreedsApplicationService;
import com.example.sampleapp.breed.domain.Breed;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedName;
import com.example.sampleapp.breed.domain.BreedNameAlreadyUsedException;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BreedsResourceTest {

  private static final ObjectMapper JSON = new ObjectMapper();
  private static final UUID FIXED_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

  @Mock
  private BreedsApplicationService service;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new BreedsResource(service)).build();
  }

  @Test
  void shouldListBreeds() throws Exception {
    when(service.findAll()).thenReturn(List.of(new Breed(new BreedId(FIXED_ID), new BreedName("cane corso"))));

    mockMvc
      .perform(get("/api/breeds"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(FIXED_ID.toString()))
      .andExpect(jsonPath("$[0].name").value("cane corso"));
  }

  @Test
  void shouldGetBreed() throws Exception {
    when(service.get(new BreedId(FIXED_ID))).thenReturn(new Breed(new BreedId(FIXED_ID), new BreedName("cane corso")));

    mockMvc.perform(get("/api/breeds/{id}", FIXED_ID)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("cane corso"));
  }

  @Test
  void shouldCreateBreed() throws Exception {
    when(service.create(any())).thenReturn(new Breed(new BreedId(FIXED_ID), new BreedName("cane corso")));

    mockMvc
      .perform(
        post("/api/breeds").contentType(MediaType.APPLICATION_JSON).content(JSON.writeValueAsString(new RestBreedToSave("cane corso")))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(FIXED_ID.toString()));
  }

  @Test
  void shouldRenameBreed() throws Exception {
    when(service.rename(eq(new BreedId(FIXED_ID)), any())).thenReturn(new Breed(new BreedId(FIXED_ID), new BreedName("labrador")));

    mockMvc
      .perform(
        put("/api/breeds/{id}", FIXED_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSON.writeValueAsString(new RestBreedToSave("labrador")))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("labrador"));
  }

  @Test
  void shouldDeleteBreed() throws Exception {
    mockMvc.perform(delete("/api/breeds/{id}", FIXED_ID)).andExpect(status().isNoContent());

    verify(service).delete(new BreedId(FIXED_ID));
  }

  @Test
  void shouldReturnNotFoundForUnknownBreed() throws Exception {
    when(service.get(any())).thenThrow(UnknownBreedException.forId(new BreedId(FIXED_ID)));

    mockMvc.perform(get("/api/breeds/{id}", FIXED_ID)).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnConflictWhenNameAlreadyUsed() throws Exception {
    when(service.create(any())).thenThrow(new BreedNameAlreadyUsedException(new BreedName("cane corso")));

    mockMvc
      .perform(
        post("/api/breeds").contentType(MediaType.APPLICATION_JSON).content(JSON.writeValueAsString(new RestBreedToSave("cane corso")))
      )
      .andExpect(status().isConflict());
  }
}
