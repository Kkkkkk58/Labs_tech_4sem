package ru.kslacker.cats.services.models.catowners;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.services.validation.annotations.ValidCatOwnerId;

@Builder
public record CatOwnerUpdateInformation(@ValidCatOwnerId Long id,
										String name,
										LocalDate dateOfBirth) {

}
