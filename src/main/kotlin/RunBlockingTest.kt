import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val logger = mu.KotlinLogging.logger {  }

fun main() = runBlocking {
    launch { // coroutine
        delay(1000L)
        logger.info { "word" }
    }

    logger.info { "hello" }
    // main함수가 blocking 모드로 동작. 내부 코루틴이 모두 작동할 때 까지 자동적으로 블로킹됨.
}