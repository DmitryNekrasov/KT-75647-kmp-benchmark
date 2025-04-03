package singlesequenceof

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
class SequenceOperationsBenchmark {

    @Benchmark
    fun benchmarkMethod(): Int {
        return 1 + 1
    }
}