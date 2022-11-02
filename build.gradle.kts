plugins {
    kotlin("jvm") version "1.7.20"
    java
}

sourceSets["main"].kotlin.srcDirs("src")
sourceSets["main"].java.srcDirs("src")

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_16
}

repositories {
    mavenCentral()
    maven("https://www.jitpack.io")
}

dependencies {
    compileOnly("com.github.Anuken.Arc:arc-core:v139")
    compileOnly("com.github.Anuken.MindustryJitpack:core:v139")

    annotationProcessor("com.github.Anuken:jabel:0.9.0")
}