package ru.kslacker.cats.presentation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.dao.CatDaoImpl;
import ru.kslacker.cats.dataaccess.dao.CatOwnerDaoImpl;
import ru.kslacker.cats.presentation.controllers.CatController;
import ru.kslacker.cats.presentation.controllers.CatOwnerController;
import ru.kslacker.cats.presentation.models.catowners.CreateCatOwnerModel;
import ru.kslacker.cats.presentation.models.cats.CreateCatModel;
import ru.kslacker.cats.presentation.models.cats.GetCatByParamsModel;
import ru.kslacker.cats.services.CatOwnerServiceImpl;
import ru.kslacker.cats.services.CatServiceImpl;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;

public class App {

	public static void main(String[] args) {
		System.out.println("Cats");
		EntityManager em = Persistence.createEntityManagerFactory("cats")
			.createEntityManager();
		CatDaoImpl catDao = new CatDaoImpl(em);
		CatOwnerDaoImpl catOwnerDao = new CatOwnerDaoImpl(em);
		CatController c = new CatController(new CatServiceImpl(em, catDao, catOwnerDao));
		CatOwnerController cc = new CatOwnerController(new CatOwnerServiceImpl(em, catOwnerDao));

		CatOwnerDto owner1 = cc.create(new CreateCatOwnerModel("Ivan Ivanov", LocalDate.now()));
		CatDto cat1 = c.create(new CreateCatModel("BBBB", LocalDate.now(), "Abyss", FurColor.GINGER,
			owner1.id()));
		CatDto cat2 = c.create(new CreateCatModel("AAAA", LocalDate.now(), "Abyss", FurColor.FAWN,
			owner1.id()));

		c.makeFriends(cat1.id(), cat2.id());
		CatOwnerDto owner = cc.get(owner1.id());
		List<CatDto> cats = c.getByBreed("Abyss");

		GetCatByParamsModel model = new GetCatByParamsModel(null, null, null, "Abyss", FurColor.GINGER, null);
		List<CatDto> result = c.getBy(model);

	}
}
