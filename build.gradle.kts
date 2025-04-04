import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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
    jvm { withJava() }
    linuxX64()
    linuxArm64()
    macosArm64()
    js(IR) { nodejs() }
    @OptIn(ExperimentalWasmDsl::class) wasmJs { nodejs() }

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

object BenchmarkConfiguration {
    const val FORK = 2
    const val BENCHMARK_MODE = "avgt"
    const val TIME_UNIT = "ns"
    const val ITERATION_TIME = 1L
    const val ITERATION_TIME_UNIT = "s"
    const val WARMUPS = 20
    const val ITERATIONS = 10
}

benchmark {
    targets {
        register("jvm")
        register("linuxX64")
        register("linuxArm64")
        register("macosArm64")
        register("js")
        register("wasmJs")
    }
    configurations {
        named("main") {
            with(BenchmarkConfiguration) {
                mode = BENCHMARK_MODE
                outputTimeUnit = TIME_UNIT
                warmups = WARMUPS
                iterations = ITERATIONS
                iterationTime = ITERATION_TIME
                iterationTimeUnit = ITERATION_TIME_UNIT
                advanced("jvmForks", FORK)
            }
        }
    }
}

jmh {
    with(BenchmarkConfiguration) {
        benchmarkMode = listOf(BENCHMARK_MODE)
        timeUnit = TIME_UNIT
        fork = FORK
        warmup = "$ITERATION_TIME$ITERATION_TIME_UNIT"
        timeOnIteration = "$ITERATION_TIME$ITERATION_TIME_UNIT"
        warmupIterations = WARMUPS
        iterations = ITERATIONS
    }
    profilers = listOf("gc")
    includes.add(".*Benchmark.*")
    failOnError = true
    resultFormat = "JSON"
    @Suppress("DEPRECATION")
    resultsFile.set(project.file("${project.buildDir}/reports/jmh/results.json"))
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}