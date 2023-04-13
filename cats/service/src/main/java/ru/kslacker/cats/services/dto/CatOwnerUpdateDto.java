package ru.kslacker.cats.services.dto;

import lombok.Builder;
import ru.kslacker.cats.services.validation.ValidCatOwnerId;
import ru.kslacker.cats.services.validation.ValidUpdateName;
import java.time.LocalDate;
import java.util.List;

@Builder
public record CatOwnerUpdateDto(@ValidCatOwnerId Long id,
								@ValidUpdateName String name,
								LocalDate dateOfBirth,
								List<Long> cats) {

}
