package ru.kslacker.cats.services.api;

import java.util.List;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.models.cats.CatInformation;
import ru.kslacker.cats.services.models.cats.CatSearchOptions;
import ru.kslacker.cats.services.models.cats.CatUpdateInformation;

public interface CatService {

	CatDto create(CatInformation catInformation);

	void delete(Long id);

	CatDto get(Long id);

	List<CatDto> getBy(CatSearchOptions searchOptions);

	void makeFriends(Long cat1Id, Long cat2Id);

	void removeFriend(Long cat1Id, Long cat2Id);

	boolean exists(Long id);

	CatDto update(CatUpdateInformation catUpdateInformation);
}
