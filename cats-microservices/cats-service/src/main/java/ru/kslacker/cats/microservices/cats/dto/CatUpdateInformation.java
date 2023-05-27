package ru.kslacker.cats.microservices.cats.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.microservices.cats.validation.annotations.ValidCatId;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;


@Builder
public record CatUpdateInformation(@ValidCatId Long id,
								   String name,
								   LocalDate dateOfBirth,
								   String breed,
								   FurColor furColor) {

}
