import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = GlobalScope.launch {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 조건식이 있으면 연산이 끝날 때 까지 루틴이 중단되지 않음
//             while(isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("sleep ${i++}")
                nextPrintTime += 500L
            }
        }
    }

    delay(1500)
    println("main: I'm tired of waiting")
    job.cancelAndJoin()
    println("main: now I can quit")
}