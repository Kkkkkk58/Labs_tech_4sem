package ru.kslacker.cats.test.dataaccess;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("ru.kslacker.cats.dataaccess")
@EntityScan("ru.kslacker.cats.dataaccess.entities")
@EnableJpaRepositories("ru.kslacker.cats.dataaccess.repositories")
public class CatsDataTestConfig {

}
