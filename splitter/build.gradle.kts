import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.4.32"
    id("java-library")
    id("maven-publish")
}

group = "de.buhrwerk.spritesplitter"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.beust:klaxon:${project.extra["klaxonVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.extra["junitVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${project.extra["junitVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${project.extra["junitVersion"]}")
    testImplementation("org.assertj:assertj-core:${project.extra["assertJVersion"]}")
    testImplementation("io.mockk:mockk:${project.extra["mockkVersion"]}")
}

publishing {
    publications {
        create<MavenPublication>("spritesplitter") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
