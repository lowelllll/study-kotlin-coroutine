import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val mutex = Mutex()
private var counter3 = 0

fun main() {
    return runBlocking {
        massiveRun {
            mutex.withLock { // 상호 배제
                counter3++
            }
        }
        println("counter = $counter3")
    }
}