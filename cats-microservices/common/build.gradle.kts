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
    implementation("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName("jar") {
	enabled = true
}