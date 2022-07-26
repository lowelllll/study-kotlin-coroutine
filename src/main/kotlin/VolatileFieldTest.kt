import kotlin.concurrent.thread

// 데이터를 캐시에 넣지 않도록 변수를 선언
// 값 쓰기에 대한 원자성이 보장되지는 않음
@Volatile private var running = false

private var count = 0

fun start() {
    running = true
    thread {
        while(running) println("${Thread.currentThread()}, count: ${count++}")
    }
}

fun stop() { running = false }

fun main() {
    start()
    start()
    Thread.sleep(10)
    stop()
}