package ru.kslacker.cats.microservices.cats.services.api;

import java.util.List;
import ru.kslacker.cats.microservices.cats.dto.CatDto;
import ru.kslacker.cats.microservices.cats.dto.CatInformation;
import ru.kslacker.cats.microservices.cats.dto.CatSearchOptions;
import ru.kslacker.cats.microservices.cats.dto.CatUpdateInformation;
import ru.kslacker.cats.microservices.cats.dto.FriendsInfo;

public interface CatService {

	CatDto create(CatInformation catInformation);

	boolean delete(Long id);

	CatDto get(Long id);

	List<CatDto> getBy(CatSearchOptions searchOptions);

	boolean makeFriends(FriendsInfo friendsInfo);

	boolean removeFriend(FriendsInfo friendsInfo);

	boolean exists(Long id);

	CatDto update(CatUpdateInformation catUpdateInformation);
}
