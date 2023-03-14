package ru.kslacker.cats.services.api;

import ru.kslacker.cats.dataaccess.entities.CatOwner;
import java.util.List;
import java.util.function.Predicate;

public interface CatOwnerService {
	CatOwner addOwner(CatOwner owner);
	void removeOwner(CatOwner owner);
	CatOwner getOwner(Long id);
	void updateOwner(CatOwner owner);
	List<CatOwner> getBy(Predicate<CatOwner> condition);
}
