package ru.kslacker.cats.microservices.catowners.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.microservices.catowners.validation.annotations.ValidCatOwnerId;

@Builder
public record CatOwnerUpdateInformation(@ValidCatOwnerId Long id,
										String name,
										LocalDate dateOfBirth) {

}
