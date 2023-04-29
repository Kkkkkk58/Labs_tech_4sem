package ru.kslacker.cats.services.models.cats;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.common.models.FurColor;

@Builder
public record CatSearchOptions(String name,
							   LocalDate dateOfBirth,
							   String breed,
							   FurColor furColor,
							   Long ownerId,
							   List<Long> friendsIds,
							   Pageable pageable) {

}
