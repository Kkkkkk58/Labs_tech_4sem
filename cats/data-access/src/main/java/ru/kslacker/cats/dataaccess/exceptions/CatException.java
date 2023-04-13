package ru.kslacker.cats.dataaccess.exceptions;

import ru.kslacker.cats.common.exceptions.CatsException;
import ru.kslacker.cats.dataaccess.entities.Cat;

public class CatException extends CatsException {

	private CatException(String message) {
		super(message);
	}

	public static CatException makingFriendsWithSelf() {
		return new CatException("Can't make friends with self");
	}

	public static CatException friendNotFound(Cat cat) {
		return new CatException("Cat " + cat + " wasn't found among friends");
	}

	public static CatException friendAlreadyExists(Cat cat, Cat friend) {
		return new CatException("Cat " + cat + " already has friend " + friend);
	}
}
