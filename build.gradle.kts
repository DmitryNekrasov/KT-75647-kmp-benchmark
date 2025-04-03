plugins {
    kotlin("multiplatform") version "2.1.10"
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
    id("me.champeau.jmh") version "0.7.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    linuxX64()
    linuxArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
            }
        }

        val jvmJmh by getting {
            dependsOn(commonMain)
        }
    }
}

benchmark {
    targets {
        register("jvm")
        register("linuxX64")
        register("linuxArm64")
    }
}

jmh {
    fork.set(2)
    profilers.set(listOf("gc"))
    includes.add(".*Benchmark.*")
    failOnError.set(true)
    resultFormat.set("JSON")
    @Suppress("DEPRECATION")
    resultsFile.set(project.file("${project.buildDir}/reports/jmh/results.json"))
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}