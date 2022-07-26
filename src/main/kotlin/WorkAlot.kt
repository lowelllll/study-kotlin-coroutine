import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    repeat(1000) { // 코루틴 생성
        launch {
            delay(1000L)
            print(".")
        }
    }
}