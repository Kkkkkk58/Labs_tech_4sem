package ru.kslacker.cats.microservices.catowners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("ru.kslacker.cats.microservices.jpaentities.entities")
public class CatOwnersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatOwnersApplication.class, args);
	}

}
