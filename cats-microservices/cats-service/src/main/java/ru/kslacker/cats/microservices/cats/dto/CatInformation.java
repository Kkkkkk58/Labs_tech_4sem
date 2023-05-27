package ru.kslacker.cats.microservices.cats.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;

@Builder
public record CatInformation(String name,
							 LocalDate dateOfBirth,
							 String breed,
							 FurColor furColor,
							 Long catOwnerId) {

}
