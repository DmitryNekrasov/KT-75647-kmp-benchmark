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
class SequencePolymorphicCallSiteBenchmark {

    @Param("default_only", "single_only", "mixed")
    private lateinit var polymorphicScenario: String
    lateinit var sequences: List<Sequence<Int>>

    @Setup
    fun setup() {
        val elements = List(100) { RANDOM.nextInt(0, 1_000_000) * 2 + 1 }
        sequences = when (polymorphicScenario) {
            "default_only" -> elements.map { sequenceOf(it) }
            "single_only" -> elements.map { singleSequenceOf(it) }
            "mixed" -> elements.mapIndexed { index, value ->
                if (index % 2 == 0) sequenceOf(value) else singleSequenceOf(value)
            }
            else -> throw IllegalArgumentException("Unknown scenario: $polymorphicScenario")
        }
    }

    @Benchmark
    fun polymorphicCallSite(blackhole: Blackhole) {
        var sum = 0
        for (seq in sequences) {
            sum += seq
                .map { it * 2 }
                .filter { it > 0 }
                .filter { it % 2 == 0 }
                .firstOrNull() ?: 0
        }
        blackhole.consume(sum)
    }

    companion object {
        val RANDOM = Random(0xcafebabe)
    }
}