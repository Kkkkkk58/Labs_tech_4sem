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
	api("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}