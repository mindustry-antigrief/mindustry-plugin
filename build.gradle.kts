plugins {
    kotlin("jvm") version "1.7.20"
    java
}

sourceSets["main"].kotlin.srcDir("src")
sourceSets["main"].java.srcDir("src")

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_16
}

repositories {
    mavenCentral()
    maven("https://www.jitpack.io")
}

dependencies {
    compileOnly("com.github.Anuken.Arc:arc-core:v140")
    compileOnly("com.github.anuken.mindustryjitpack:core:v140")

    annotationProcessor("com.github.Anuken:jabel:0.9.0")
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    from(rootDir){
        include("plugin.hjson")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}