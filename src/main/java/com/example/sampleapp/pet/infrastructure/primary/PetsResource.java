package com.example.sampleapp.pet.infrastructure.primary;

import com.example.sampleapp.breed.domain.UnknownBreedException;
import com.example.sampleapp.pet.application.PetsApplicationService;
import com.example.sampleapp.pet.domain.PetId;
import com.example.sampleapp.pet.domain.UnknownPetException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
class PetsResource {

  private final PetsApplicationService pets;

  PetsResource(PetsApplicationService pets) {
    this.pets = pets;
  }

  @GetMapping
  List<RestPet> list() {
    return pets.findAll().stream().map(RestPet::from).toList();
  }

  @GetMapping("/{id}")
  RestPet get(@PathVariable UUID id) {
    return RestPet.from(pets.get(new PetId(id)));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestPet create(@Valid @RequestBody RestPetToSave payload) {
    return RestPet.from(pets.create(payload.toDomainCreate()));
  }

  @PutMapping("/{id}")
  RestPet update(@PathVariable UUID id, @Valid @RequestBody RestPetToSave payload) {
    return RestPet.from(pets.update(new PetId(id), payload.toDomainName(), payload.toDomainBreedId()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable UUID id) {
    pets.delete(new PetId(id));
  }

  @ExceptionHandler(UnknownPetException.class)
  ResponseEntity<String> handleUnknownPet(UnknownPetException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(UnknownBreedException.class)
  ResponseEntity<String> handleUnknownBreed(UnknownBreedException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
}
