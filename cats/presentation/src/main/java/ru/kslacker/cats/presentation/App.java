package ru.kslacker.cats.presentation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class App {

	public static void main(String[] args) {
		System.out.println("Cats");
		EntityManager em = Persistence.createEntityManagerFactory("cats")
			.createEntityManager();
//		CatController c = new CatController(new CatServiceImpl(em, new CatDaoImpl(em), new CatOwnerDaoImpl(em)));
//		CatOwner owner = new CatOwner("owner", LocalDate.now());
//		CatOwnerController cc = new CatOwnerController(new CatOwnerServiceImpl(em, new CatOwnerDaoImpl(em)));
//
//		CatOwnerDto dto = cc.create(new CreateCatOwnerModel(owner.getName(), owner.getDateOfBirth()));
//		c.create(new CreateCatModel("vasya", LocalDate.now(), "Boris", FurColor.BLACK, dto.id()));
	}
}
