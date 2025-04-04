package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
class SequenceBasicCreationBenchmark {

    @Param("1", "10", "1000", "1000000")
    var creationCount: Int = 0

    @Benchmark
    fun sequenceOfCreationDefault(blackhole: Blackhole) {
        repeat(creationCount) {
            val seq = sequenceOf(1)
            blackhole.consume(seq)
        }
    }

    @Benchmark
    fun sequenceOfCreationSingle(blackhole: Blackhole) {
        repeat(creationCount) {
            val seq = singleSequenceOf(1)
            blackhole.consume(seq)
        }
    }
}