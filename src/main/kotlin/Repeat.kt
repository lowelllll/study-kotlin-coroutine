import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun repeatGlobalLaunch() = GlobalScope.launch { // GlobakScope라서 main 스레드가 종료되면 같이 종료됨
    // launch는 main스레드라서 그런건가?
    // main에서 바로실행되서?
    repeat(1000) { i ->
        println("sleep $i")
        delay(500L)
    }
}

fun CoroutineScope.repeatJob() =  launch {
    repeat(1000) { i ->
        println("sleep $i")
        delay(500L)
    }
}


fun CoroutineScope.tryCatch() = launch {
    try {
        repeat(1000) { i ->
            println("sleep $i")
            delay(500L)
        }
    } finally {
        withContext(NonCancellable) { // 완전한 실행을 보장함
            println("bye!")
            delay(1000L)
            println("NonCancellable")
        }

    }
}

fun main() =  runBlocking {
    val job = tryCatch()
    delay(1300L)
//    job.cancel() // 1300ms 이후 캔슬
    job.cancelAndJoin() // 작업을 취소하고 완료될 때 까지 기다림
}

