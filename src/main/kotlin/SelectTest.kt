import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.util.Random

fun main() {
    return runBlocking {
        val routine1 = GlobalScope.produce {
            delay(Random().nextInt(1000).toLong())
            send("A")
        }

        val routine2 = GlobalScope.produce {
            delay(Random().nextInt(1000).toLong())
            send("B")
        }

        val result = select<String> {  // 먼저 수행된 것을 받음
            routine1.onReceive { it }
            routine2.onReceive { it }
        }

        println("result : $result")
    }
}