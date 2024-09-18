plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.http.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.json:json:20231013")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
