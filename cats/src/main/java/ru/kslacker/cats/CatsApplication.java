package ru.kslacker.cats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// TODO tests
// TODO Specifications instead of map
// TODO Hide swagger on release versions (profiles!!) and hide not rest controllers
// TODO html pages + controllers

@SpringBootApplication
public class CatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsApplication.class, args);
	}
}
