package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

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
}