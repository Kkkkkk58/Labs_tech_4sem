package ru.kslacker.cats.microservices.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CatOwnerInformation(@NotBlank String name, @PastOrPresent LocalDate dateOfBirth) {

}
