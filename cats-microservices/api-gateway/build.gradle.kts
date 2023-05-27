plugins {
    id("java")
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "ru.kslacker.cats.microservices"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.0.5")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.1")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
	implementation("io.micrometer:micrometer-tracing")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}