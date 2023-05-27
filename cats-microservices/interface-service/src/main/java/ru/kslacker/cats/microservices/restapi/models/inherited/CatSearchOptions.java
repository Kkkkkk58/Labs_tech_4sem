package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;

@Builder
public record CatSearchOptions(String name,
							   LocalDate dateOfBirth,
							   String breed,
							   FurColor furColor,
							   Long ownerId,
							   List<Long> friendsIds,
							   Pageable pageable) {

}
