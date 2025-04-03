package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlin.random.Random

@State(Scope.Benchmark)
class SequenceOperationsBenchmark {

    // Basic creation benchmarks
    @Benchmark
    fun sequenceOfCreationDefault(blackhole: Blackhole, state: CreationState) {
        repeat(state.count) {
            val seq = sequenceOf(1)
            blackhole.consume(seq)
        }
    }

    @Benchmark
    fun sequenceOfCreationSingle(blackhole: Blackhole, state: CreationState) {
        repeat(state.count) {
            val seq = singleSequenceOf(1)
            blackhole.consume(seq)
        }
    }

    @State(Scope.Benchmark)
    class CreationState {
        @Param("1", "10", "1000", "1000000")
        var count: Int = 0
    }

    // Terminal operation benchmarks
    @Benchmark
    fun sequenceFirst(blackhole: Blackhole, state: SequenceState) {
        var result = state.sequence.first()
        blackhole.consume(result)
    }

    // Chain of transformations
    @Benchmark
    fun sequenceChain(blackhole: Blackhole, state: SequenceState) {
        val result = state.sequence
            .map { it * 3 }
            .filter { (it and 1) == 1 }
            .firstOrNull()
        blackhole.consume(result)
    }

    // Real-world scenario
    @Benchmark
    fun sequenceRealWorld(blackhole: Blackhole, state: SequenceState) {
        val baseValue = 78
        val result = state.sequence
            .map { it + baseValue }
            .map { it * 3 }
            .filter { (it and 1) == 1 }
            .map { it.toString() }
            .map { it.length }
            .sum()
        blackhole.consume(result)
    }

    @State(Scope.Benchmark)
    class SequenceState {
        @Param("default", "single")
        private lateinit var type: String

        lateinit var sequence: Sequence<Int>

        @Setup
        fun setup() {
            val element = RANDOM.nextInt(0, 1_000_000) * 2 + 1
            sequence = when (type) {
                "default" -> sequenceOf(element)
                "single" -> singleSequenceOf(element)
                else -> throw IllegalArgumentException("Unknown sequence type: $type")
            }
        }
    }

    companion object {
        val RANDOM = Random(0xcafebabe)
    }
}