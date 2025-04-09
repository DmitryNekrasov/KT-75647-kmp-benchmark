package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
class SequenceBasicCreationBenchmark {

    @Benchmark
    fun singleSequenceOfCreationDefault(blackhole: Blackhole) {
        val seq = sequenceOf(1)
        blackhole.consume(seq)
    }

    @Benchmark
    fun singleSequenceOfCreationSingle(blackhole: Blackhole) {
        val seq = singleSequenceOf(1)
        blackhole.consume(seq)
    }

    @Benchmark
    fun emptySequenceOfCreationDefault(blackhole: Blackhole) {
        val seq = sequenceOf<Int>()
        blackhole.consume(seq)
    }

    @Benchmark
    fun emptySequenceOfCreationSingle(blackhole: Blackhole) {
        val seq = emptySequenceOf<Int>()
        blackhole.consume(seq)
    }
}