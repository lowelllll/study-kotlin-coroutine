import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// https://kotlinworld.com/144?category=973476

// coroutine option
// Dkotlinx.coroutines.debug
private val logger = mu.KotlinLogging.logger {  }

fun main() {
    loggingInThread("start")
    return runBlocking {
        val job3 = CoroutineScope(Dispatchers.IO).async {
            loggingInThread("job3")
            (1..1000).sortedByDescending { it }
        }

        val job1 = CoroutineScope(Dispatchers.Unconfined).async { // main
            loggingInThread("job1")

            val job3Result = job3.await()

            job3Result.forEach {
                loggingInThread("$it")
            }
        }

        val job2 = CoroutineScope(Dispatchers.Unconfined).launch { // main
            loggingInThread("job2")
        }

        job1.await()
    }

//    runBlocking {
//        val job3 = CoroutineScope(Dispatchers.IO).async {
//            loggingInThread("job3")
//            (1..1000).sortedByDescending { it }
//        }
//
//        val job1 = async(Dispatchers.Default) { // main
//            loggingInThread("job1")
//        }
//
//        val job2 = launch(Dispatchers.Default) { // main
//            loggingInThread("job2")
//
//            val job3Result = job3.await()
//
//            job3Result.forEach {
//                loggingInThread("$it") // 왜 이건..
//            }
//        }
//    }

//    return runBlocking {
//        async (Dispatchers.IO) {
//            logger.info { "IO" }
//        }
//    }
}

fun loggingInThread(str: String) {
    logger.info { str }
}