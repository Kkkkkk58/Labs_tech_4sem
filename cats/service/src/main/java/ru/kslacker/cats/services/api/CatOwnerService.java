package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.CatOwnerUpdateDto;

public interface CatOwnerService {

	CatOwnerDto create(String name, LocalDate dateOfBirth);

	void delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(String name, LocalDate dateOfBirth, List<Long> catsIds,
		Pageable pageable);

	boolean exists(Long id);

	CatOwnerDto update(CatOwnerUpdateDto catOwnerDto);
}
