package ru.kslacker.cats.services.models;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.validation.annotations.ValidCatId;
import ru.kslacker.cats.services.validation.annotations.UpdateDate;
import ru.kslacker.cats.services.validation.annotations.UpdateName;

@Builder
public record CatUpdateModel(@ValidCatId Long id,
							 @UpdateName String name,
							 @UpdateDate LocalDate dateOfBirth,
							 String breed,
							 FurColor furColor) {

}
