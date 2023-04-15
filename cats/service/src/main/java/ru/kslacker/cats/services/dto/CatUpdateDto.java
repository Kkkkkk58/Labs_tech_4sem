package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.validation.ValidCatId;
import ru.kslacker.cats.services.validation.ValidUpdateCatOwnerId;
import ru.kslacker.cats.services.validation.ValidUpdateName;

@Builder
public record CatUpdateDto(@ValidCatId Long id,
						   @ValidUpdateName String name,
						   LocalDate dateOfBirth,
						   String breed,
						   FurColor furColor,
						   @ValidUpdateCatOwnerId Long ownerId) {

}
