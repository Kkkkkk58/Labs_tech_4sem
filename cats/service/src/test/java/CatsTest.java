import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import mocks.MockEntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.CatOwnerServiceImpl;
import ru.kslacker.cats.services.CatServiceImpl;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;


public class CatsTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private CatDao catDao;

	@Mock
	private CatOwnerDao catOwnerDao;

	private CatOwnerService catOwnerService;
	private CatService catService;

	@BeforeEach
	public void setup() {

		entityManager = mock(EntityManager.class);
		when(entityManager.getTransaction()).thenReturn(new MockEntityTransaction());
		catDao = mock(CatDao.class);
		catOwnerDao = mock(CatOwnerDao.class);

		catService = new CatServiceImpl(entityManager, catDao, catOwnerDao);
		catOwnerService = new CatOwnerServiceImpl(entityManager, catOwnerDao);
	}

	@Test
	public void createOwner_ownerSaved() {
		String name = "Aboba Aboba";
		LocalDate birthDate = LocalDate.of(1992, 10, 1);
		CatOwner catOwner = new CatOwner(name, birthDate);
		when(catOwnerDao.getById(1L)).thenReturn(catOwner);

		CatOwnerDto catOwnerDto = catOwnerService.get(1L);

		Assertions.assertEquals(name, catOwnerDto.name());
		Assertions.assertEquals(birthDate, catOwnerDto.dateOfBirth());
	}

	@Test
	public void createCat_catSavedAndAddedToOwner() {
		String name = "Test cat";
		LocalDate birthDate = LocalDate.of(1995, 10, 11);
		String breed = "Russian";
		FurColor furColor = FurColor.GREY;

		CatOwner owner = getTestOwner();
		Cat cat = new Cat(name, birthDate, breed, furColor, owner);
		when(catOwnerDao.getById(1L)).thenReturn(owner);
		when(catDao.getById(1L)).thenReturn(cat);

		CatDto catDto = catService.get(1L);
		CatOwnerDto catOwnerDto = catOwnerService.get(1L);

		Assertions.assertEquals(name, catDto.name());
		Assertions.assertEquals(birthDate, catDto.dateOfBirth());
		Assertions.assertEquals(breed, catDto.breed());
		Assertions.assertEquals(furColor, catDto.furColor());
		Assertions.assertEquals(catOwnerDto.id(), catDto.owner().id());
		Assertions.assertTrue(catOwnerDto.cats().contains(catDto.id()));
	}

	@Test
	public void makeFriends_addedFriendsToCollections() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catDao.getById(1L)).thenReturn(cat1);
		when(catDao.getById(2L)).thenReturn(cat2);

		catService.makeFriends(1L, 2L);
		CatDto cat1Dto = catService.get(1L);
		CatDto cat2Dto = catService.get(2L);

		Assertions.assertTrue(
			cat1.getFriends().stream().filter(c -> c.getName().equals("1")).toList().isEmpty());
		Assertions.assertTrue(
			cat2.getFriends().stream().filter(c -> c.getName().equals("2")).toList().isEmpty());
	}

	@Test
	public void endFriendship_removedFriendsFromCollections() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catDao.getById(1L)).thenReturn(cat1);
		when(catDao.getById(2L)).thenReturn(cat2);

		catService.makeFriends(1L, 2L);
		catService.removeFriend(1L, 2L);
		CatDto cat1Dto = catService.get(1L);
		CatDto cat2Dto = catService.get(2L);

		Assertions.assertTrue(
			cat1.getFriends().stream().filter(c -> c.getName().equals("2")).toList().isEmpty());
		Assertions.assertTrue(
			cat2.getFriends().stream().filter(c -> c.getName().equals("1")).toList().isEmpty());
	}

	@Test
	public void deleteCat_removedFromFriendsAndOwnersCats() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catOwnerDao.getById(1L)).thenReturn(owner);
		when(catDao.getById(1L)).thenReturn(cat1);
		when(catDao.getById(2L)).thenReturn(cat2);

		catService.makeFriends(1L, 2L);
		catService.remove(1L);
		CatDto cat2Dto = catService.get(2L);
		CatOwnerDto ownerDto = catOwnerService.get(1L);

		Assertions.assertThrows(RuntimeException.class, () -> catService.get(1L));
		Assertions.assertTrue(
			cat2.getFriends().stream().filter(c -> c.getName().equals("1")).toList().isEmpty());
		Assertions.assertTrue(
			owner.getCats().stream().filter(c -> c.getName().equals("1")).toList().isEmpty());
	}

	private CatOwner getTestOwner() {
		return new CatOwner("Test owner", LocalDate.now());
	}

	private Cat getTestCat(CatOwner owner, String name) {
		return new Cat(
			name,
			LocalDate.now(),
			"Test breed",
			FurColor.BLACK,
			owner);
	}

	private Cat getTestCat(CatOwner owner) {
		return getTestCat(owner, "Test cat");
	}
}
