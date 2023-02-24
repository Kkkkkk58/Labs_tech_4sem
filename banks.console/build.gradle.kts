plugins {
    id("java")
	id("application")
	id("org.springframework.boot") version "3.0.3" apply false
	id("io.spring.dependency-management") version "1.1.0"
}


group = "ru.kslacker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
	implementation(project(":banks"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
	mainClass.set("ru.kslacker.banks.console.Program")
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}