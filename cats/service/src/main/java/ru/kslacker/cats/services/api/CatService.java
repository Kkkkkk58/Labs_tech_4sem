package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.services.dto.CatDto;

public interface CatService {

	CatDto create(String name, LocalDate dateOfBirth, String breed, FurColor furColor, Long catOwnerId);
	void remove(Long id);
	CatDto get(Long id);
	List<CatDto> getByName(String name);
	List<CatDto> getByDateOfBirth(LocalDate dateOfBirth);
	List<CatDto> getByOwner(Long ownerId);
	List<CatDto> getByColor(FurColor color);
	List<CatDto> getByBreed(String breed);
	List<CatDto> getBy(Predicate<Cat> condition);
	List<CatDto> getBy(Map<String, Object> paramSet);
	void makeFriends(Long cat1Id, Long cat2Id);
	void removeFriends(Long cat1Id, Long cat2Id);
}
