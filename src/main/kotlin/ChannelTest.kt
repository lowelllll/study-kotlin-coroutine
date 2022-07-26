import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    return runBlocking {
        val channel = Channel<Int>() // 자료를 주고 받기 위한 약속된 일종의 통로
        launch {
            for (x in 1..5)  channel.send(x)
            channel.close() // 채널 닫기
        }
//        repeat(5) { println(channel.receive())}
        for (element in channel) { println(element)}
        println("done")
    }
    // SendChannel에 채널이 꽉 차있는지 화인해야함. 꽉 차있으면 일시 지연됨
    // SendChannel.isFull: true면 채널이 꽉 차있는거고 일시 지연됨
    // SendChannel.isClosedForSend: close() 의해 닫으면 true로 지정되서 isFull이 false가 됨
}