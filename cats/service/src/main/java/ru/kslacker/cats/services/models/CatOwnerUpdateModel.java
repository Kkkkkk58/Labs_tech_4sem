package ru.kslacker.cats.services.models;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.services.validation.annotations.ValidCatOwnerId;
import ru.kslacker.cats.services.validation.annotations.UpdateDate;
import ru.kslacker.cats.services.validation.annotations.UpdateName;

@Builder
public record CatOwnerUpdateModel(@ValidCatOwnerId Long id,
								  @UpdateName String name,
								  @UpdateDate LocalDate dateOfBirth) {

}
