package ru.kslacker.cats.microservices.catowners.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CatOwnerInformation(String name, LocalDate dateOfBirth) {

}
