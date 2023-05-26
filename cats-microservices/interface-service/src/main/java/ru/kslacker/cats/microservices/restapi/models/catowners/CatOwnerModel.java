package ru.kslacker.cats.microservices.restapi.models.catowners;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import ru.kslacker.cats.microservices.restapi.validation.ValidationGroup;
import ru.kslacker.cats.microservices.restapi.validation.annotations.PastOrPresentUpdateDate;
import ru.kslacker.cats.microservices.restapi.validation.annotations.UpdateName;
import java.time.LocalDate;

public record CatOwnerModel(@NotBlank(groups = ValidationGroup.OnCreate.class)
							@UpdateName(groups = ValidationGroup.OnUpdate.class)
							String name,

							@PastOrPresent(groups = ValidationGroup.OnCreate.class)
							@PastOrPresentUpdateDate(groups = ValidationGroup.OnUpdate.class)
							LocalDate dateOfBirth) {

}
