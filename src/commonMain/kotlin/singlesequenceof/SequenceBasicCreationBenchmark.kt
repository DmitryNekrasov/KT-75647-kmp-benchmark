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
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 1)
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