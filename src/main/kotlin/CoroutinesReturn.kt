import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun doWork1(): String {
    delay(1000)
    return "work1"
}

suspend fun doWork2(): String {
    delay(3000)
    return "work2"
}


fun worksInSerial() {
    GlobalScope.launch { // 코루틴 실행
        val one = doWork1()
        val two = doWork2()
        println("one: $one")
        println("two: $two")

    }
}


fun worksInParallel() {
    val one = GlobalScope.async { // 병행 수행 가능
        doWork1()
    }

    val two = GlobalScope.async {
        doWork2()
    }

    GlobalScope.launch {
        val oneResult = one.await()
        println("one: $oneResult")
        val twoResult = two.await()
        println("two: $twoResult")
    }
}

fun main() {
    worksInParallel()
    println("hello")
    readLine() // main 스레드 종료 방지
}