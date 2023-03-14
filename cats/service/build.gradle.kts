plugins {
    id("java")
	id("org.hibernate.orm") version "6.2.0.CR3"
}

group = "ru.kslacker"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
	implementation(project(":cats:data-access"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}