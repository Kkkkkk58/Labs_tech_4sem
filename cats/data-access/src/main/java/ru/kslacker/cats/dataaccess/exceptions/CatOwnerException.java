package ru.kslacker.cats.dataaccess.exceptions;

import ru.kslacker.cats.common.exceptions.CatsException;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

public class CatOwnerException extends CatsException {

	private CatOwnerException(String message) {
		super(message);
	}

	public static CatOwnerException catAlreadyExists(CatOwner catOwner, Cat cat) {
		return new CatOwnerException("Cat " + cat + " is already added to " + catOwner);
	}

	public static CatOwnerException catNotFound(CatOwner catOwner, Cat cat) {
		return new CatOwnerException("Cat " + cat + " doesn't belong to owner " + catOwner);
	}
}
