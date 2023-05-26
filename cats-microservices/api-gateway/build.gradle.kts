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
	implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.0.5")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.1")
	implementation("io.micrometer:micrometer-tracing-bridge-brave:1.1.1")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave:2.16.4")
	implementation("io.micrometer:micrometer-observation:1.11.0")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus:1.11.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}