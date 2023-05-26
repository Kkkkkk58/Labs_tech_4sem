package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;

@Builder
public record CatUpdateInformation(Long id,
								   String name,
								   LocalDate dateOfBirth,
								   String breed,
								   FurColor furColor) {

}
