package ru.kslacker.cats.microservices.restapi.services.api;

import ru.kslacker.cats.microservices.restapi.models.inherited.CatDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatUpdateInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.FriendsInfo;
import java.util.List;

public interface CatService {

	CatDto create(CatInformation catInformation);

	void delete(Long id);

	CatDto get(Long id);

	List<CatDto> getBy(CatSearchOptions searchOptions);

	void makeFriends(FriendsInfo friendsInfo);

	void removeFriend(FriendsInfo friendsInfo);

	boolean exists(Long id);

	CatDto update(CatUpdateInformation catUpdateInformation);

}
