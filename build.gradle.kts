import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
}

group = "com.stsf"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    repositories {
        mavenCentral()
    }

//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
//    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:1.5.9")
//    implementation("io.springfox:springfox-swagger2:3.0.0")
//    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.5.6")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.6")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.hibernate:hibernate-core:5.6.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.andreapivetta.kolor:kolor:1.0.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    runtimeOnly("org.postgresql:postgresql:42.3.1")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")
    implementation("org.jetbrains:markdown:0.2.4")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
