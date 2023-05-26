plugins {
    id("java")
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "ru.kslacker.cats.microservices"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
	implementation(project(":cats-microservices:common"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.security:spring-security-core:6.1.0")
	implementation("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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