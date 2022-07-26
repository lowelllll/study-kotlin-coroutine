import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

// https://elizarov.medium.com/structured-concurrency-722d765aa952

suspend fun parallelDecomposition() = CoroutineScope(Dispatchers.Unconfined).async {
    val deferred1: Deferred<String> = async { delay(1000); throw RuntimeException() }
    val deferred2: Deferred<String> = async {
        printlnWithThread("deffered2 start")
        delay(1000)
        printlnWithThread("deffered2 progress...")
        delay(500)
        printlnWithThread("deffered2 end")
        "deffered2"
    }
}

fun main() {
    val job= runBlocking {
        val deferred1:Deferred<String> = async { delay(1000); throw RuntimeException() }
        val deferred2: Deferred<String> = async {
            printlnWithThread("deffered2 start")
            delay(1000)
            printlnWithThread("deffered2 progress...")
            delay(500)
            printlnWithThread("deffered2 end")
            "deffered2"
        }
        printlnWithThread(listOf(deferred1.await(), deferred2.await()).joinToString())
    }
}


//fun main() = runBlocking<Unit> {
//    launch { // context of the parent, main runBlocking coroutine
//        printlnWithThread("main runBlocking : I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.Unconfined) {
//        printlnWithThread("Unconfined : I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.Default) {
//        printlnWithThread("Default : I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.Unconfined) {
//        printlnWithThread("Unconfined : I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    launch(newSingleThreadContext()) {
//        printlnWithThread("Default : I'm working in thread ${Thread.currentThread().name}")
//
//        launch(Dispatchers.Unconfined) {
//            printlnWithThread("Unconfined : I'm working in thread ${Thread.currentThread().name}")
//        }
//    }
//
//    launch(Dispatchers.Unconfined) {
//        printlnWithThread("Unconfined : I'm working in thread ${Thread.currentThread().name}")
//    }
//}