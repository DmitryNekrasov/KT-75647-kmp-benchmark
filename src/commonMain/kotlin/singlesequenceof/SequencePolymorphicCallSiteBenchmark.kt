package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlin.random.Random

@State(Scope.Benchmark)
class SequencePolymorphicCallSiteBenchmark {

    @Param("default_only", "single_only", "mixed")
    lateinit var polymorphicScenario: String
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
        var sum = 0L
        for (seq in sequences) {
            sum += seq
                .map { it * 2 }
                .map(Int::toLong)
                .filter { it > 0 }
                .filter { it % 2 == 0L }
                .firstOrNull() ?: 0
        }
        blackhole.consume(sum)
    }

    companion object {
        val RANDOM = Random(0xcafebabe)
    }
}