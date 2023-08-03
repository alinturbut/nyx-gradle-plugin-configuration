import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.mooltiverse.oss.nyx.gradle.NyxExtension

// Apply plugins
plugins {
    kotlin("jvm") version "1.6.21"
    id("org.springframework.boot") version "2.6.14"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("io.freefair.lombok") version "6.6.2"
    `nyx-plugin`
    java
}

group = "io.alinturbut"
version = System.getenv("NYX_VERSION")
description = "Nyx Gradle Plugin Configuration"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:3.3.0")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Apache Commons
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
}

tasks.bootJar {
    enabled = false
}

// Build executable jar
tasks.jar {
    enabled = true
    // Remove `plain` postfix from jar file name
    archiveClassifier.set("")
}

tasks.nyxInfer.get().dependsOn("configureNyx")
tasks.nyxPublish.get().dependsOn("configureNyx")
