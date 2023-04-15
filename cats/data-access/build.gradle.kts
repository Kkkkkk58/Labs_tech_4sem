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
	api(project(":cats:common"))
	api("org.springframework.boot:spring-boot-starter-data-jpa")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	implementation("org.postgresql:postgresql:42.2.27")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2:2.1.214")
	testImplementation("org.liquibase:liquibase-core:4.20.0")
	testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
	testImplementation("org.dbunit:dbunit:2.6.0")
}

tasks.getByName<Test>("test") {
	useJUnitPlatform()
}

tasks.getByName("bootJar") {
	enabled = false
}

tasks.getByName("jar") {
	enabled = true
}