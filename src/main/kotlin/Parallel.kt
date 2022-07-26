import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// 일시 중단, 제개
fun main() {
    return runBlocking {
        launch {
            repeat(5) { i ->
                printlnWithThread("coroutine A, $i")
                delay(10)
            }
        }
        launch {
            repeat(5) { i ->
                printlnWithThread("coroutine B, $i")
                delay(10)
            }
        }
    }
}