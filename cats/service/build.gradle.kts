plugins {
    id("java-library")
	id("org.hibernate.orm") version "6.2.0.CR3"
}

group = "ru.kslacker"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
	api(project(":cats:data-access"))
	implementation("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}