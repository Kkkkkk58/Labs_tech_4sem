package ru.kslacker.cats.presentation;

import static ru.kslacker.cats.dataaccess.utils.DbService.getSessionFactory;

import ru.kslacker.cats.dataaccess.dao.CatDaoImpl;
import ru.kslacker.cats.dataaccess.dao.CatOwnerDaoImpl;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;
import ru.kslacker.cats.presentation.controllers.CatController;
import ru.kslacker.cats.presentation.controllers.CatOwnerController;
import ru.kslacker.cats.services.CatOwnerServiceImpl;
import ru.kslacker.cats.services.CatServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;

public class App {

	public static void main(String[] args) {
		System.out.println("Cats");
		CatController c = new CatController(new CatServiceImpl(new CatDaoImpl(getSessionFactory())));
		Cat vasya = new Cat();
		vasya.setName("vasya");
		vasya.setBreed(Breed.A);
		vasya.setFurColor(FurColor.BLACK);
		vasya.setDateOfBirth(LocalDate.now());

		CatOwner owner = new CatOwner();
		owner.setName("LOSHARA");
		owner.setDateOfBirth(LocalDate.now());
		owner.setCats(new ArrayList<>());

		vasya.setOwner(owner);
		vasya.setFriends(new ArrayList<>());

		CatOwnerController cc = new CatOwnerController(new CatOwnerServiceImpl(new CatOwnerDaoImpl(getSessionFactory())));

		cc.create(owner);
		c.create(vasya);
	}
}
