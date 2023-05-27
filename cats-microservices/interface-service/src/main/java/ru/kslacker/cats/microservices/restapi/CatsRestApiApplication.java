package ru.kslacker.cats.microservices.restapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("ru.kslacker.cats.microservices.jpaentities.entities")
@OpenAPIDefinition(info = @Info(title = "Cats API", version = "3.0", description = "API for the service for registration of cats and their owners"))
@SecurityScheme(name = "BasicAuth", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class CatsRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsRestApiApplication.class, args);
	}

}
