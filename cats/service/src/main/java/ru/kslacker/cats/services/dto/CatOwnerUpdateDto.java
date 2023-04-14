package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.services.validation.ValidCatOwnerId;
import ru.kslacker.cats.services.validation.ValidUpdateName;

@Builder
public record CatOwnerUpdateDto(@ValidCatOwnerId Long id,
								@ValidUpdateName String name,
								LocalDate dateOfBirth) {

}
