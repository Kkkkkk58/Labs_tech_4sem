package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import lombok.Builder;
@Builder
public record CatOwnerUpdateInformation(Long id,
										String name,
										LocalDate dateOfBirth) {

}
