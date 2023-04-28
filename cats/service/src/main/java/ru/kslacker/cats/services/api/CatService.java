package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.models.CatUpdateModel;

public interface CatService {

	CatDto create(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
		Long catOwnerId);

	void delete(Long id);

	CatDto get(Long id);

	List<CatDto> getBy(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
		Long ownerId, List<Long> friendsIds, Pageable pageable);

	void makeFriends(Long cat1Id, Long cat2Id);

	void removeFriend(Long cat1Id, Long cat2Id);

	boolean exists(Long id);

	CatDto update(CatUpdateModel catDto);
}
