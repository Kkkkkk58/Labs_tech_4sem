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
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications;
import ru.kslacker.cats.dataaccess.specifications.CatOwnerFieldsSpecifications;

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


	@Autowired
	public CatsDataTest(
		DataSource dataSource,
		JdbcTemplate jdbcTemplate,
		EntityManager entityManager,
		CatRepository catRepository,
		CatOwnerRepository catOwnerRepository) {

		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
		this.entityManager = entityManager;
		this.catRepository = catRepository;
		this.catOwnerRepository = catOwnerRepository;
	}

	@Test
	public void injectComponents_componentsAreNotNull() {
		Assertions.assertNotNull(dataSource);
		Assertions.assertNotNull(jdbcTemplate);
		Assertions.assertNotNull(entityManager);
		Assertions.assertNotNull(catRepository);
		Assertions.assertNotNull(catOwnerRepository);
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
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void initializedEntities_entitiesAreFound() {
		Optional<CatOwner> owner = catOwnerRepository.findById(1L);
		Optional<Cat> cat1 = catRepository.findById(1L);
		Optional<Cat> cat2 = catRepository.findById(2L);

		Assertions.assertTrue(owner.isPresent());
		Assertions.assertTrue(cat1.isPresent());
		Assertions.assertTrue(cat2.isPresent());
		Assertions.assertEquals(owner.get(), cat1.get().getOwner());
		Assertions.assertTrue(cat1.get().getFriends().contains(cat2.get()));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithNameSpecification_searchWorks() {
		String name = "Test";
		Specification<Cat> specification = where(CatFieldSpecifications.withName(name));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getName().equals(name)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithDateOfBirthSpecification_searchWorks() {
		LocalDate dateOfBirth = LocalDate.of(2007, 12, 12);
		Specification<Cat> specification = where(
			CatFieldSpecifications.withDateOfBirth(dateOfBirth));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(
			cats.stream().allMatch(cat -> cat.getDateOfBirth().equals(dateOfBirth)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithBreedSpecification_searchWorks() {
		String breed = "Test";
		Specification<Cat> specification = where(CatFieldSpecifications.withBreed(breed));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getBreed().equals(breed)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithFurColorSpecification_searchWorks() {
		FurColor color = FurColor.BROWN;
		Specification<Cat> specification = where(CatFieldSpecifications.withFurColor(color));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getFurColor().equals(color)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithOwnerIdSpecification_searchWorks() {
		Long ownerId = 1L;
		Specification<Cat> specification = where(CatFieldSpecifications.withOwnerId(ownerId));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(
			cats.stream().allMatch(cat -> cat.getOwner().getId().equals(ownerId)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithFriendSpecification_searchWorks() {
		Cat friend = catRepository.findById(2L).orElseThrow();
		Specification<Cat> specification = where(CatFieldSpecifications.withFriend(friend));
		List<Cat> cats = catRepository.findAll(specification);

		Assertions.assertFalse(cats.isEmpty());
		Assertions.assertTrue(cats.stream().allMatch(cat -> cat.getFriends().contains(friend)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatWithCustomCriteriaSpecification_searchWorks() {
		FurColor color = FurColor.BROWN;
		Specification<Cat> specification = where(CatFieldSpecifications.withFurColor(color));
		List<Cat> catsWithColor = catRepository.findAll(specification);
		Long ownerId = 1L;
		specification = specification.and(CatFieldSpecifications.withOwnerId(ownerId));
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
		Specification<CatOwner> specification = where(CatOwnerFieldsSpecifications.withName(name));
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
			CatOwnerFieldsSpecifications.withDateOfBirth(dateOfBirth));
		List<CatOwner> catOwners = catOwnerRepository.findAll(specification);

		Assertions.assertFalse(catOwners.isEmpty());
		Assertions.assertTrue(
			catOwners.stream().allMatch(catOwner -> catOwner.getDateOfBirth().equals(dateOfBirth)));
	}

	@Test
	@DatabaseSetup("classpath:dbUnit/data.xml")
	public void searchCatOwnerWithCatSpecification_searchWorks() {
		Cat cat = catRepository.findById(1L).orElseThrow();
		Specification<CatOwner> specification = where(CatOwnerFieldsSpecifications.withCat(cat));
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
		Specification<CatOwner> specification = where(CatOwnerFieldsSpecifications.withName(name));
		List<CatOwner> catOwnersWithName = catOwnerRepository.findAll(specification);
		specification = specification.and(
			CatOwnerFieldsSpecifications.withDateOfBirth(dateOfBirth));
		List<CatOwner> catOwnersWithNameAndDateOfBirth = catOwnerRepository.findAll(specification);

		Assertions.assertTrue(catOwnersWithNameAndDateOfBirth.stream()
			.allMatch(catOwner -> catOwner.getName().equals(name) && catOwner.getDateOfBirth()
				.equals(dateOfBirth)));
		Assertions.assertNotEquals(catOwnersWithName, catOwnersWithNameAndDateOfBirth);
	}


	private CatOwner getTestOwner() {
		String name = "KSlacker";
		LocalDate dateOfBirth = LocalDate.of(2003, 5, 7);
		CatOwner owner = new CatOwner(name, dateOfBirth);

		return catOwnerRepository.saveAndFlush(owner);
	}
}


