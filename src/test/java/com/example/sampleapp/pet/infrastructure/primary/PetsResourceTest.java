package com.example.sampleapp.pet.infrastructure.primary;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.sampleapp.UnitTest;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.UnknownBreedException;
import com.example.sampleapp.pet.application.PetsApplicationService;
import com.example.sampleapp.pet.domain.Pet;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.PetName;
import com.example.sampleapp.pet.domain.UnknownPetException;
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
class PetsResourceTest {

  private static final ObjectMapper JSON = new ObjectMapper();
  private static final UUID PET_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private static final UUID BREED_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

  @Mock
  private PetsApplicationService service;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new PetsResource(service)).build();
  }

  private Pet umee() {
    return new Pet(new PetId(PET_ID), new PetName("umee"), new BreedId(BREED_ID));
  }

  @Test
  void shouldListPets() throws Exception {
    when(service.findAll()).thenReturn(List.of(umee()));

    mockMvc
      .perform(get("/api/pets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(PET_ID.toString()))
      .andExpect(jsonPath("$[0].breedId").value(BREED_ID.toString()));
  }

  @Test
  void shouldGetPet() throws Exception {
    when(service.get(new PetId(PET_ID))).thenReturn(umee());

    mockMvc.perform(get("/api/pets/{id}", PET_ID)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("umee"));
  }

  @Test
  void shouldCreatePet() throws Exception {
    when(service.create(any())).thenReturn(umee());

    mockMvc
      .perform(
        post("/api/pets").contentType(MediaType.APPLICATION_JSON).content(JSON.writeValueAsString(new RestPetToSave("umee", BREED_ID)))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(PET_ID.toString()));
  }

  @Test
  void shouldUpdatePet() throws Exception {
    when(service.update(eq(new PetId(PET_ID)), any(), any())).thenReturn(umee());

    mockMvc
      .perform(
        put("/api/pets/{id}", PET_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSON.writeValueAsString(new RestPetToSave("umee", BREED_ID)))
      )
      .andExpect(status().isOk());
  }

  @Test
  void shouldDeletePet() throws Exception {
    mockMvc.perform(delete("/api/pets/{id}", PET_ID)).andExpect(status().isNoContent());

    verify(service).delete(new PetId(PET_ID));
  }

  @Test
  void shouldReturnNotFoundForUnknownPet() throws Exception {
    when(service.get(any())).thenThrow(UnknownPetException.forId(new PetId(PET_ID)));

    mockMvc.perform(get("/api/pets/{id}", PET_ID)).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForUnknownBreed() throws Exception {
    when(service.create(any())).thenThrow(UnknownBreedException.forId(new BreedId(BREED_ID)));

    mockMvc
      .perform(
        post("/api/pets").contentType(MediaType.APPLICATION_JSON).content(JSON.writeValueAsString(new RestPetToSave("umee", BREED_ID)))
      )
      .andExpect(status().isBadRequest());
  }
}
