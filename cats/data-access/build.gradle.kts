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
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}