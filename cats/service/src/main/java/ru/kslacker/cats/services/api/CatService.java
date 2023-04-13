package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.services.dto.CatDto;

public interface CatService {

	CatDto create(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
		Long catOwnerId);

	void delete(Long id);

	CatDto get(Long id);

	List<CatDto> getBy(Predicate<Cat> condition);

	List<CatDto> getBy(Map<String, Object> paramSet);

	void makeFriends(Long cat1Id, Long cat2Id);

	void removeFriend(Long cat1Id, Long cat2Id);
}
