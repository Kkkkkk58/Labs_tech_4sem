import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.dao.CatDaoImpl;
import ru.kslacker.cats.dataaccess.dao.CatOwnerDaoImpl;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.services.CatOwnerServiceImpl;
import ru.kslacker.cats.services.CatServiceImpl;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;


public class CatsTest {

	private static EntityManagerFactory emf;
	private CatOwnerService catOwnerService;
	private CatService catService;

	@BeforeAll
	public static void initialSetup() {
		emf = Persistence.createEntityManagerFactory("cats-test");
	}

	@BeforeEach
	public void setup() {
		EntityManager em = emf.createEntityManager();
		CatOwnerDao catOwnerDao = new CatOwnerDaoImpl(em);
		CatDao catDao = new CatDaoImpl(em);
		this.catOwnerService = new CatOwnerServiceImpl(em, catOwnerDao);
		this.catService = new CatServiceImpl(em, catDao, catOwnerDao);
	}

	@Test
	public void createOwner_ownerSaved() {
		String name = "Aboba Aboba";
		LocalDate birthDate = LocalDate.of(1992, 10, 1);
		CatOwnerDto catOwnerDto = catOwnerService.create(name, birthDate);

		Assertions.assertNotNull(catOwnerDto.id());
		Assertions.assertEquals(name, catOwnerDto.name());
		Assertions.assertEquals(birthDate, catOwnerDto.dateOfBirth());
	}

	@Test
	public void createCat_catSavedAndAddedToOwner() {
		CatOwnerDto catOwnerDto = getTestOwner();
		String name = "Test cat";
		LocalDate birthDate = LocalDate.of(1995, 10, 11);
		String breed = "Russian";
		FurColor furColor = FurColor.GREY;

		CatDto catDto = catService.create(name, birthDate, breed, furColor, catOwnerDto.id());
		catOwnerDto = catOwnerService.get(catOwnerDto.id());

		Assertions.assertNotNull(catDto.id());
		Assertions.assertEquals(name, catDto.name());
		Assertions.assertEquals(birthDate, catDto.dateOfBirth());
		Assertions.assertEquals(breed, catDto.breed());
		Assertions.assertEquals(furColor, catDto.furColor());
		Assertions.assertEquals(catOwnerDto.id(), catDto.owner().id());
		Assertions.assertTrue(catOwnerDto.cats().contains(catDto.id()));
	}

	@Test
	public void makeFriends_addedFriendsToCollections() {
		CatOwnerDto owner = getTestOwner();
		CatDto cat1 = getTestCat(owner.id());
		CatDto cat2 = getTestCat(owner.id());

		catService.makeFriends(cat1.id(), cat2.id());
		cat1 = catService.get(cat1.id());
		cat2 = catService.get(cat2.id());

		Assertions.assertTrue(cat1.friends().contains(cat2.id()));
		Assertions.assertTrue(cat2.friends().contains(cat1.id()));
	}

	@Test
	public void endFriendship_removedFriendsFromCollections() {
		CatOwnerDto owner = getTestOwner();
		CatDto cat1 = getTestCat(owner.id());
		CatDto cat2 = getTestCat(owner.id());

		catService.makeFriends(cat1.id(), cat2.id());
		catService.removeFriend(cat1.id(), cat2.id());
		cat1 = catService.get(cat1.id());
		cat2 = catService.get(cat2.id());

		Assertions.assertFalse(cat1.friends().contains(cat2.id()));
		Assertions.assertFalse(cat2.friends().contains(cat1.id()));
	}

	@Test
	public void deleteCat_removedFromFriendsAndOwnersCats() {
		CatOwnerDto owner = getTestOwner();
		CatDto cat1 = getTestCat(owner.id());
		CatDto cat2 = getTestCat(owner.id());
		catService.makeFriends(cat1.id(), cat2.id());

		catService.remove(cat1.id());
		cat2 = catService.get(cat2.id());
		owner = catOwnerService.get(owner.id());

		Assertions.assertThrows(RuntimeException.class, () -> catService.get(cat1.id()));
		Assertions.assertFalse(cat2.friends().contains(cat1.id()));
		Assertions.assertFalse(owner.cats().contains(cat1.id()));
	}

	private CatOwnerDto getTestOwner() {
		return catOwnerService.create("Test owner", LocalDate.now());
	}

	private CatDto getTestCat(Long ownerId) {
		return catService.create(
			"Test cat",
			LocalDate.now(),
			"Test breed",
			FurColor.BLACK,
			ownerId);
	}
}
