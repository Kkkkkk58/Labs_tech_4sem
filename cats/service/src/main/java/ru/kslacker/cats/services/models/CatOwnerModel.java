package ru.kslacker.cats.services.models;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CatOwnerModel(String name, LocalDate dateOfBirth) {

}
