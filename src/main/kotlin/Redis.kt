import jdk.nashorn.internal.objects.Global
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    return runBlocking {
        launch { redis() }
        launch { mysql() }
    }
}
//
//fun main() {
//    return runBlocking {
//        CoroutineScope(Dispatchers.IO).launch {
//            redis()
//            mysql()
//        }.join()
//    }
//}

suspend fun redis() {
    printlnWithThread("redis start")
    delay(1000)
    printlnWithThread("redis end")

}

suspend fun mysql() {
    printlnWithThread("mysql start")
    delay(2000)
    printlnWithThread("mysql end")
}