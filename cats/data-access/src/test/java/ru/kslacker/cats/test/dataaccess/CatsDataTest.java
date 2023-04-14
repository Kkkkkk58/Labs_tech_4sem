package ru.kslacker.cats.test.dataaccess;

import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;

@DataJpaTest
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

}


