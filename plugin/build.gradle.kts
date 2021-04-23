plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("com.gradle.plugin-publish") version "0.14.0"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(gradleKotlinDsl())

    implementation(project(":splitter"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.extra["junitVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${project.extra["junitVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${project.extra["junitVersion"]}")
    testImplementation("org.assertj:assertj-core:${project.extra["assertJVersion"]}")
}

gradlePlugin {
    plugins.create("spriteSplitter") {
        id = "de.buhrwerk.spritesplitter"
        implementationClass = "de.buhrwerk.spritesplitter.SpriteSplitterPlugin"
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
