import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

fun CoroutineScope.producer(): ReceiveChannel<Int> = produce { // 채널이 붙어있는 생산자
    var total = 0
    for (x in 1..5) {
        total += x
        send(total) // 생산자
    }
}

fun main() {
    return runBlocking {
        val result = producer()
        result.consumeEach { println(it) } // 소비자 루틴
    }
}