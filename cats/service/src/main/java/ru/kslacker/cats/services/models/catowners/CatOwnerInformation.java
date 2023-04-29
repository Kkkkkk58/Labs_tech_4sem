package ru.kslacker.cats.services.models.catowners;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CatOwnerInformation(String name, LocalDate dateOfBirth) {

}
