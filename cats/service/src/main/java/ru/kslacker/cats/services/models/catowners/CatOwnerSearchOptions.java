package ru.kslacker.cats.services.models.catowners;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record CatOwnerSearchOptions(
	String name,
	LocalDate dateOfBirth,
	List<Long> catsIds,
	Pageable pageable) {

}
