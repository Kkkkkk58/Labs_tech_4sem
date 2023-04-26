package ru.kslacker.cats;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO tests
// TODO GET with RSQL/Specifications extended query with custom operators
// TODO Hide swagger on release versions (profiles!!)
// TODO html pages + controllers (+ err page and controller advice)
// TODO better security scheme than basic

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Cats API", version = "3.0", description = "API for the service for registration of cats and their owners"))
@SecurityScheme(name = "BasicAuth", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class CatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsApplication.class, args);
	}
}
