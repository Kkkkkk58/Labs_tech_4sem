plugins {
    id("java")
	id("application")
}

group = "ru.kslacker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
	implementation(project(":banks"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
	mainClass.set("ru.kslacker.banks.console.Program")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}