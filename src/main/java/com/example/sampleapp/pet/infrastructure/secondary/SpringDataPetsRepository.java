package com.example.sampleapp.pet.infrastructure.secondary;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPetsRepository extends JpaRepository<PetEntity, UUID> {}
