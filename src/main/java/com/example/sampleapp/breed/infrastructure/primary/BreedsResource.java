package com.example.sampleapp.breed.infrastructure.primary;

import com.example.sampleapp.breed.application.BreedsApplicationService;
import com.example.sampleapp.breed.domain.BreedId;
import com.example.sampleapp.breed.domain.BreedNameAlreadyUsedException;
import com.example.sampleapp.breed.domain.UnknownBreedException;
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
@RequestMapping("/api/breeds")
class BreedsResource {

  private final BreedsApplicationService breeds;

  BreedsResource(BreedsApplicationService breeds) {
    this.breeds = breeds;
  }

  @GetMapping
  List<RestBreed> list() {
    return breeds.findAll().stream().map(RestBreed::from).toList();
  }

  @GetMapping("/{id}")
  RestBreed get(@PathVariable UUID id) {
    return RestBreed.from(breeds.get(new BreedId(id)));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestBreed create(@Valid @RequestBody RestBreedToSave payload) {
    return RestBreed.from(breeds.create(payload.toDomainCreate()));
  }

  @PutMapping("/{id}")
  RestBreed rename(@PathVariable UUID id, @Valid @RequestBody RestBreedToSave payload) {
    return RestBreed.from(breeds.rename(new BreedId(id), payload.toDomainName()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable UUID id) {
    breeds.delete(new BreedId(id));
  }

  @ExceptionHandler(UnknownBreedException.class)
  ResponseEntity<String> handleUnknown(UnknownBreedException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(BreedNameAlreadyUsedException.class)
  ResponseEntity<String> handleConflict(BreedNameAlreadyUsedException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}
