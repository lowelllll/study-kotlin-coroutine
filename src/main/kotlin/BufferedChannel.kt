import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    return runBlocking { // 현재 thread를 블로킹 (main스레드)
        val channel = Channel<Int>(3)
        val sender = launch(coroutineContext) {
            repeat(10) {
                println("Sending $it")
                channel.send(it) // 지속적으로 보내다가 버퍼가 꽉차면 일시 지연
            }
        }
        channel.receive()
        channel.receive()
        channel.receive()

        delay(2000)
        sender.cancel() // 아무것도 안받고 종료
    }
}