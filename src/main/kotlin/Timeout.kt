import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main()  = runBlocking {
    try {
        withTimeout(1300L) { // 일정 시간 뒤 코루틴을 취소시킨다.
            repeat(1000) { i ->
                println("sleep $i")
                delay(500L)
            }
        }
    } catch (e: TimeoutCancellationException) {
        println("timed out with $e")
    }
}