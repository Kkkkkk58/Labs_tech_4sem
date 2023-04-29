package ru.kslacker.cats.test.dataaccess;

import static org.springframework.data.jpa.domain.Specification.where;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.dataaccess.exceptions.UserBuilderException;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.dataaccess.specifications.CatSpecifications;
import ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications;
import ru.kslacker.cats.dataaccess.specifications.UserSpecifications;

@DataJpaTest
@TestPropertySource(properties = {
	"spring.jpa.hibernate.ddl-auto=validate"
})
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
public class CatsDataTest {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	private final EntityManager entityManager;
	private final CatRepository catRepository;
	private final CatOwnerRepository catOwnerRepository;
	private final UserRepository userRepository;

	@Autowired
	public CatsDataTest(
		DataSource dataSource,
		JdbcTemplate jdbcTemplate,
		EntityManager entityManager,
		CatRepository catRepository,
		CatOwnerRepository catOwnerRepository,
		UserRepository userRepository) {

		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
		this.entityManager = entityManager;
		this.catRepository = catRepository;
		this.catOwnerRepository = catOwnerRepository;
		this.userRepository = userRepository;
	}

	@Test
	public void injectComponents_componentsAreNotNull() {
		Assertions.assertNotNull(dataSource);
		Assertions.assertNotNull(jdbcTemplate);
		Assertions.assertNotNull(entityManager);
		Assertions.assertNotNull(catRepository);
		Assertions.assertNotNull(catOwnerRepository);
		Assertions.assertNotNull(userRepository);
	}

	@Test
	public void createOwner_ownerSaved() {
		String name = "KSlacker";
		LocalDate dateOfBirth = LocalDate.of(2003, 5, 7);
		CatOwner owner = new CatOwner(name, dateOfBirth);

		owner = catOwnerRepository.saveAndFlush(owner);

		Assertions.assertNotNull(owner.getId());
		Assertions.assertEquals(name, owner.getName());
		Assertions.assertEquals(dateOfBirth, owner.getDateOfBirth());
		Assertions.assertTrue(owner.getCats().isEmpty());
	}

	@Test
	public void createCat_catSaved() {
		CatOwner owner = getTestOwner();
		String name = "Cat";
		LocalDate dateOfBirth = LocalDate.of(2009, 12, 11);
		String breed = "Test";
		FurColor color = FurColor.BROWN;
		Cat cat = new Cat(name, dateOfBirth, breed, color, owner);

		cat = catRepository.saveAndFlush(cat);

		Assertions.assertNotNull(cat.getId());
		Assertions.assertEquals(name, cat.getName());
		Assertions.assertEquals(dateOfBirth, cat.getDateOfBirth());
		Assertions.assertEquals(breed, cat.getBreed());
		Assertions.assertEquals(color, cat.getFurColor());
		Assertions.assertEquals(owner, cat.getOwner());
	}

	@Test
	public void createUserWithCredentialsAndDefaults_userSavedWithDefaults() {
		String username = "kslacker";
		String password = "123654";

		User user = User.builder()
			.withUsername(username)
			.withPassword(password)
			.build();

		user = userRepository.saveAndFlush(user);

		Assertions.assertNotNull(user.getId());
		Assertions.assertEquals(username, user.getUsername());
		Assertions.assertNull(user.getEmail());
		Assertions.assertEquals(password, user.getPassword());
		Assertions.assertEquals(UserRole.USER, user.getRole());
		Assertions.assertTrue(user.isEnabled());
		Assertions.assertFalse(user.isLocked());
		Assertions.assertNull(user.getAccountExpirationDate());
		Assertions.assertNull(user.getCredentialsExpirationDate());
	}

	@Test
	public void createUserWithCustomParameters_userSavedWithParameters() {
		String username = "kslacker";
		String password = "123654";
		UserRole role = UserRole.ADMIN;
		boolean locked = true;
		boolean enabled = false;
		LocalDate accountExpirationDate = LocalDate.now();

		User user = User.builder()
			.withUsername(username)
			.withPassword(password)
			.withRole(role)
			.isEnabled(enabled)
			.isLocked(locked)
			.withAccountExpirationDate(accountExpirationDate)
			.build();

		user = userRepository.saveAndFlush(user);

		Assertions.assertNotNull(user.getId());
		Assertions.assertEquals(username, user.getUsername());
		Assertions.assertNull(user.getEmail());
		Assertions.assertEquals(password, user.getPassword());
		Assertions.assertEquals(role, user.getRole());
		Assertions.assertEquals(enabled, user.isEnabled());
		Assertions.assertEquals(locked, user.isLocked());
		Assertions.assertEquals(accountExpirationDate, user.getAccountExpirationDate());
		Assertions.assertNull(user.getCredentialsExpirationDate());
	}

	@Test
	public void createUserWithoutRequiredField_throwsException() {

		Assertions.assertThrows(UserBuilderException.class, () -> User.builder()
			.withUsername(null)
			.withPassword("test")
			.build());
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void initializedEntities_entitiesAreFound() {
		Optional<CatOwner> owner = catOwnerRepository.findById(1L);
		Optional<Cat> cat1 = catRepository.findById(1L);
		Optional<Cat> cat2 = catRepository.findById(2L);
		Optional<User> user = userRepository.findById(1L);

		Assertions.assertTrue(owner.isPresent());
		Assertions.assertTrue(cat1.isPresent());
		Assertions.assertTrue(cat2.isPresent());
		Assertions.assertTrue(user.isPresent());
		Assertions.assertEquals(owner.get(), cat1.get().getOwner());
		Assertions.assertTrue(cat1.get().getFriends().contains(cat2.get()));
		Assertions.assertEquals(owner.get(), user.get().getOwner());
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithNameSpecification_searchWorks() {
		String name = "Test";
		Specification<Cat> specification = where(CatSpecifications.withName(name));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getName().equals(name)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithDateOfBirthSpecification_searchWorks() {
		LocalDate dateOfBirth = LocalDate.of(2007, 12, 12);
		Specification<Cat> specification = where(
			CatSpecifications.withDateOfBirth(dateOfBirth));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(
			cats.stream().allMatch(cat -> cat.getDateOfBirth().equals(dateOfBirth)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithBreedSpecification_searchWorks() {
		String breed = "Test";
		Specification<Cat> specification = where(CatSpecifications.withBreed(breed));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getBreed().equals(breed)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithFurColorSpecification_searchWorks() {
		FurColor color = FurColor.BROWN;
		Specification<Cat> specification = where(CatSpecifications.withFurColor(color));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getFurColor().equals(color)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithOwnerIdSpecification_searchWorks() {
		Long ownerId = 1L;
		Specification<Cat> specification = where(CatSpecifications.withOwnerId(ownerId));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(
			cats.stream().allMatch(cat -> cat.getOwner().getId().equals(ownerId)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithFriendSpecification_searchWorks() {
		Cat friend = catRepository.findById(2L).orElseThrow();
		Specification<Cat> specification = where(CatSpecifications.withFriend(friend));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getFriends().contains(friend)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithCustomCriteriaSpecification_searchWorks() {
		FurColor color = FurColor.BROWN;
		Specification<Cat> specification = where(CatSpecifications.withFurColor(color));
		List<Cat> catsWithColor = catRepository.findAll(specification);
		Long ownerId = 1L;
		specification = specification.and(CatSpecifications.withOwnerId(ownerId));
		List<Cat> catsWithColorAndOwner = catRepository.findAll(specification);

		Assertions.assertTrue(catsWithColorAndOwner.stream()
			.allMatch(
				cat -> cat.getFurColor().equals(color) && cat.getOwner().getId().equals(ownerId)));
		Assertions.assertNotEquals(catsWithColor, catsWithColorAndOwner);
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatOwnerWithNameSpecification_searchWorks() {
		String name = "KSlacker";
		Specification<CatOwner> specification = where(CatOwnerSpecifications.withName(name));
		List<CatOwner> catOwners = catOwnerRepository.findAll(specification);

		Assertions.assertFalse(catOwners.isEmpty());
		Assertions.assertTrue(
			catOwners.stream().allMatch(catOwner -> catOwner.getName().equals(name)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatOwnerWithDateOfBirthSpecification_searchWorks() {
		LocalDate dateOfBirth = LocalDate.of(2003, 5, 7);
		Specification<CatOwner> specification = where(
			CatOwnerSpecifications.withDateOfBirth(dateOfBirth));
		List<CatOwner> catOwners = catOwnerRepository.findAll(specification);

		Assertions.assertFalse(catOwners.isEmpty());
		Assertions.assertTrue(
			catOwners.stream().allMatch(catOwner -> catOwner.getDateOfBirth().equals(dateOfBirth)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatOwnerWithCatSpecification_searchWorks() {
		Cat cat = catRepository.findById(1L).orElseThrow();
		Specification<CatOwner> specification = where(CatOwnerSpecifications.withCat(cat));
		List<CatOwner> catOwners = catOwnerRepository.findAll(specification);

		Assertions.assertFalse(catOwners.isEmpty());
		Assertions.assertEquals(1, catOwners.size());
		Assertions.assertTrue(
			catOwners.stream().allMatch(catOwner -> catOwner.getCats().contains(cat)));

	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatOwnerWithCustomCriteriaSpecification_searchWorks() {
		String name = "KSlacker";
		LocalDate dateOfBirth = LocalDate.of(2003, 5, 7);
		Specification<CatOwner> specification = where(CatOwnerSpecifications.withName(name));
		List<CatOwner> catOwnersWithName = catOwnerRepository.findAll(specification);
		specification = specification.and(
			CatOwnerSpecifications.withDateOfBirth(dateOfBirth));
		List<CatOwner> catOwnersWithNameAndDateOfBirth = catOwnerRepository.findAll(specification);

		Assertions.assertTrue(catOwnersWithNameAndDateOfBirth.stream()
			.allMatch(catOwner -> catOwner.getName().equals(name) && catOwner.getDateOfBirth()
				.equals(dateOfBirth)));
		Assertions.assertNotEquals(catOwnersWithName, catOwnersWithNameAndDateOfBirth);
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchUserWithUsername_searchWorks() {

		String username = "a";
		Optional<User> user = userRepository.findByUsername(username);

		Assertions.assertTrue(user.isPresent());
		Assertions.assertEquals(username, user.get().getUsername());
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchUserWithRole_searchWorks() {

		UserRole role = UserRole.ADMIN;
		Specification<User> specification = where(UserSpecifications.withRole(role));
		List<User> users = userRepository.findAll(specification);

		Assertions.assertTrue(users.stream().allMatch(user -> user.getRole().equals(role)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchUserWithLock_searchWorks() {

		boolean locked = true;
		Specification<User> specification = where(UserSpecifications.withLock(locked));
		List<User> users = userRepository.findAll(specification);

		Assertions.assertTrue(users.stream().allMatch(user -> user.isLocked() == locked));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchUserWithStatus_searchWorks() {

		boolean enabled = true;
		Specification<User> specification = where(UserSpecifications.withStatus(enabled));
		List<User> users = userRepository.findAll(specification);

		Assertions.assertTrue(users.stream().allMatch(user -> user.isEnabled() == enabled));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchUserWithAccountExpirationDate_searchWorks() {

		LocalDate accountExpirationDate = LocalDate.of(2025, 10, 10);
		Specification<User> specification = where(UserSpecifications.withAccountExpirationDate(accountExpirationDate));
		List<User> users = userRepository.findAll(specification);

		Assertions.assertTrue(users.stream().allMatch(user -> user.getAccountExpirationDate().equals(accountExpirationDate)));
	}

	private CatOwner getTestOwner() {
		String name = "KSlacker";
		LocalDate dateOfBirth = LocalDate.of(2003, 5, 7);
		CatOwner owner = new CatOwner(name, dateOfBirth);

		return catOwnerRepository.saveAndFlush(owner);
	}
}


