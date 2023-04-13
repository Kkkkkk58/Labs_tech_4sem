package ru.kslacker.cats.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


// TODO tests
// TODO Specifications instead of map
// TODO Hide swagger on release versions (profiles!!) and hide not rest controllers
// TODO Better openAPI docs
// TODO html pages + controllers

@SpringBootApplication(scanBasePackages = {"ru.kslacker.cats"})
@EntityScan({"ru.kslacker.cats.dataaccess.entities"})
@EnableJpaRepositories({"ru.kslacker.cats.dataaccess.repositories"})
public class CatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsApplication.class, args);
	}
}
