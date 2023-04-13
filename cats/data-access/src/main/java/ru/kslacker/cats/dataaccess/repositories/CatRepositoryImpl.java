package ru.kslacker.cats.dataaccess.repositories;

import org.springframework.stereotype.Component;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.repositories.api.AbstractRepository;

@Component
public class CatRepositoryImpl extends AbstractRepository<Cat, Long> {

	public CatRepositoryImpl() {
		super(Cat.class);
	}
}
