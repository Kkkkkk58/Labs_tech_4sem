package ru.kslacker.cats.test.presentation;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("ru.kslacker.cats.presentation")
@EntityScan("ru.kslacker.cats.dataaccess.entities")
public class CatsWebTestConfig {

}
