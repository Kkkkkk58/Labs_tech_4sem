package ru.kslacker.cats.dataaccess.repositories;

import org.springframework.stereotype.Component;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.api.AbstractRepository;

@Component
public class CatOwnerRepositoryImpl extends AbstractRepository<CatOwner, Long> {

	public CatOwnerRepositoryImpl() {
		super(CatOwner.class);
	}
}
