package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.services.validation.ValidCatOwnerId;
import ru.kslacker.cats.services.validation.UpdateDate;
import ru.kslacker.cats.services.validation.UpdateName;

@Builder
public record CatOwnerUpdateDto(@ValidCatOwnerId Long id,
								@UpdateName String name,
								@UpdateDate LocalDate dateOfBirth) {

}
