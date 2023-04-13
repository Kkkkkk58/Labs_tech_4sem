plugins {
    id("java-library")
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "ru.kslacker.cats"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
	api(project(":cats:service"))
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("com.github.therapi:therapi-runtime-javadoc:0.15.0")
	annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
	implementation("org.liquibase:liquibase-core:4.20.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}