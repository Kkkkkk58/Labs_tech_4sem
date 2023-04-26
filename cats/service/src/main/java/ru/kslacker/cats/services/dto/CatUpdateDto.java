package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.validation.ValidCatId;
import ru.kslacker.cats.services.validation.UpdateDate;
import ru.kslacker.cats.services.validation.UpdateName;

@Builder
public record CatUpdateDto(@ValidCatId Long id,
						   @UpdateName String name,
						   @UpdateDate LocalDate dateOfBirth,
						   String breed,
						   FurColor furColor) {

}
