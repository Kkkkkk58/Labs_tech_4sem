package ru.kslacker.cats.test.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.CatOwnerServiceImpl;
import ru.kslacker.cats.services.CatServiceImpl;
import ru.kslacker.cats.services.UserServiceImpl;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.validation.service.api.ValidationService;

@Component
public class CatsServicesTest {

	private PasswordEncoder encoder;

	private CatRepository catRepository;

	private CatOwnerRepository catOwnerRepository;
	private UserRepository userRepository;

	private CatOwnerService catOwnerService;
	private CatService catService;
	private UserService userService;

	@BeforeEach
	public void setup() {
		catRepository = mock(CatRepository.class);
		catOwnerRepository = mock(CatOwnerRepository.class);
		userRepository = mock(UserRepository.class);
		encoder = mock(PasswordEncoder.class);
		ValidationService validator = mock(ValidationService.class);

		catService = new CatServiceImpl(validator, catRepository, catOwnerRepository);
		catOwnerService = new CatOwnerServiceImpl(validator, catOwnerRepository, catRepository);
		userService = new UserServiceImpl(validator, encoder, userRepository, catOwnerRepository);
	}

	@Test
	public void createOwner_ownerSaved() {
		String name = "Aboba Aboba";
		LocalDate birthDate = LocalDate.of(1992, 10, 1);
		CatOwner catOwner = new CatOwner(name, birthDate);
		when(catOwnerRepository.findById(1L)).thenReturn(Optional.of(catOwner));

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
		when(catOwnerRepository.findById(1L)).thenReturn(Optional.of(owner));
		when(catRepository.findById(1L)).thenReturn(Optional.of(cat));

		CatDto catDto = catService.get(1L);
		CatOwnerDto catOwnerDto = catOwnerService.get(1L);

		Assertions.assertEquals(name, catDto.name());
		Assertions.assertEquals(birthDate, catDto.dateOfBirth());
		Assertions.assertEquals(breed, catDto.breed());
		Assertions.assertEquals(furColor, catDto.furColor());
		Assertions.assertEquals(catOwnerDto.id(), catDto.ownerId());
		Assertions.assertTrue(catOwnerDto.cats().contains(catDto.id()));
	}

	@Test
	public void makeFriends_addedFriendsToCollections() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catRepository.findById(1L)).thenReturn(Optional.of(cat1));
		when(catRepository.findById(2L)).thenReturn(Optional.of(cat2));

		catService.makeFriends(1L, 2L);

		Assertions.assertTrue(
			cat1.getFriends().stream().anyMatch(c -> c.getName().equals("2")));
		Assertions.assertTrue(
			cat2.getFriends().stream().anyMatch(c -> c.getName().equals("1")));
	}

	@Test
	public void endFriendship_removedFriendsFromCollections() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catRepository.findById(1L)).thenReturn(Optional.of(cat1));
		when(catRepository.findById(2L)).thenReturn(Optional.of(cat2));

		catService.makeFriends(1L, 2L);
		catService.removeFriend(1L, 2L);

		Assertions.assertTrue(
			cat1.getFriends().stream().noneMatch(c -> c.getName().equals("2")));
		Assertions.assertTrue(
			cat2.getFriends().stream().noneMatch(c -> c.getName().equals("1")));
	}

	@Test
	public void deleteCat_removedFromFriendsAndOwnersCats() {
		CatOwner owner = getTestOwner();
		Cat cat1 = getTestCat(owner, "1");
		Cat cat2 = getTestCat(owner, "2");
		when(catOwnerRepository.findById(1L)).thenReturn(Optional.of(owner));
		when(catRepository.findById(1L)).thenReturn(Optional.of(cat1));
		when(catRepository.findById(2L)).thenReturn(Optional.of(cat2));

		catService.makeFriends(1L, 2L);
		catService.delete(1L);

		Assertions.assertTrue(
			cat2.getFriends().stream().noneMatch(c -> c.getName().equals("1")));
		Assertions.assertTrue(
			owner.getCats().stream().noneMatch(c -> c.getName().equals("1")));
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
}
