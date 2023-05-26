package ru.kslacker.cats.microservices.cats.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CatSearchOptions(String name,
							   LocalDate dateOfBirth,
							   String breed,
							   FurColor furColor,
							   Long ownerId,
							   List<Long> friendsIds,
							   Pageable pageable) {

}
