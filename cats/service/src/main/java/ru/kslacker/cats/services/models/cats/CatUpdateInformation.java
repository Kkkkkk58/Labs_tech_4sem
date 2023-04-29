package ru.kslacker.cats.services.models.cats;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.validation.annotations.ValidCatId;

@Builder
public record CatUpdateInformation(@ValidCatId Long id,
								   String name,
								   LocalDate dateOfBirth,
								   String breed,
								   FurColor furColor) {

}
