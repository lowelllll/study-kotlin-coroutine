import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

// 쓰레드 가두기?
private val counterContext = newSingleThreadContext("counterContext")
private var counter2 = 0

private suspend fun massiveRun2(context: CoroutineContext, action: suspend () -> Unit) {
    val n = 1000
    val k = 1000

    val time = measureTimeMillis {
        val jobs = List(n) {
            GlobalScope.launch(context) {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("completed ${n * k} action in $time")
}

fun main() {
    return runBlocking {
        massiveRun { // 스레드 가둑ㅣ.
            counter2 ++
        }
        println("counter: $counter2")
    }
}


