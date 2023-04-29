package ru.kslacker.cats.services.models.cats;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;

@Builder
public record CatInformation(String name,
							 LocalDate dateOfBirth,
							 String breed,
							 FurColor furColor,
							 Long catOwnerId) {

}
