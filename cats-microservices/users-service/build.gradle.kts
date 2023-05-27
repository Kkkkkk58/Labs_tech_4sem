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
	implementation(project(":cats-microservices:common"))
	implementation(project(":cats-microservices:jpa-entities"))
	implementation(project(":cats-microservices:utils"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.1")
	implementation("org.springframework.security:spring-security-core:6.1.0")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
	implementation("io.micrometer:micrometer-tracing")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")
	implementation("org.postgresql:postgresql:42.2.27")
	implementation("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}