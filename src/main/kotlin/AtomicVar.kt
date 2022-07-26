import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

// 무결성 보장이 안됨
//var counter = 0

var counter = AtomicInteger(0)

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 1000
    val k = 1000

    val time = measureTimeMillis {
        val jobs = List(n) {
            GlobalScope.launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("completed ${n * k} action in $time")
}

fun main() {
    return runBlocking {
        massiveRun {
            counter.incrementAndGet()
        }
        println("counter = ${counter.get()}")
    }

}