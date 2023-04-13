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
	api(project(":cats:data-access"))
	api("org.springframework.boot:spring-boot-starter-validation")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}