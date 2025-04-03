plugins {
    kotlin("multiplatform") version "2.1.10"
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvm()
    linuxX64()
    linuxArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
            }
        }
    }
}

benchmark {
    targets {
        register("jvm")
        register("linuxX64")
        register("linuxArm64")
    }
    configurations {
        named("main") {
            mode = "avgt"
            outputTimeUnit = "ns"
            warmups = 10
            iterations = 5
            iterationTime = 1
            iterationTimeUnit = "s"
        }
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}