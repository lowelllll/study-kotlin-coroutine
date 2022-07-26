import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val job = launch { // background에서 실행하는 작업
        delay(1000L)
        println("world!")
    }
    println("hello")

    job.join() // 코루틴이 완료되길 기다림
}