package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import kotlin.random.Random

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 1)
class SequenceOperationsBenchmark {

    @Param("default", "single")
    private lateinit var sequenceType: String
    lateinit var sequence: Sequence<Int>

    @Setup
    fun setup() {
        val element = RANDOM.nextInt(0, 1_000_000) * 2 + 1
        sequence = when (sequenceType) {
            "default" -> sequenceOf(element)
            "single" -> singleSequenceOf(element)
            else -> throw IllegalArgumentException("Unknown sequence type: $sequenceType")
        }
    }

    // Terminal operation benchmarks
    @Benchmark
    fun sequenceFirst(blackhole: Blackhole) {
        var result = sequence.first()
        blackhole.consume(result)
    }

    // Chain of transformations
    @Benchmark
    fun sequenceChain(blackhole: Blackhole) {
        val result = sequence
            .map { it * 3 }
            .filter { (it and 1) == 1 }
            .firstOrNull()
        blackhole.consume(result)
    }

    // Real-world scenario
    @Benchmark
    fun sequenceRealWorld(blackhole: Blackhole) {
        val baseValue = 78
        val result = sequence
            .map { it + baseValue }
            .map { it * 3 }
            .filter { (it and 1) == 1 }
            .map { it.toString() }
            .map { it.length }
            .sum()
        blackhole.consume(result)
    }

    companion object {
        val RANDOM = Random(0xcafebabe)
    }
}