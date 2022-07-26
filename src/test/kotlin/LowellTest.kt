import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class LowellTest {

    @Test
    fun `중첩된 runBlocking test`() {
        return runBlocking(Dispatchers.Default) {
            printlnWithThread("runBlocking before")


            runBlocking(Dispatchers.IO) { // default thread blocking됨
                suspendF("redis", 1)
                suspendF("mysql", 3)
//                throw RuntimeException()
            }

            CoroutineScope(Dispatchers.IO).launch{ // default thread blocking되지 않음
                suspendF("pms", 2)
            }
//            runBlocking(Dispatchers.IO) {
//                suspendF("pms", 2)
//            }

            printlnWithThread("runBlocking after")

        }
    }

    @Test
    fun `순차적인 코루틴`() { // 순차적으로 실행됨 같은 코루틴 안에 있어서 그런듯?
        GlobalScope.launch { // default thread blocking됨
            suspendF("redis", 3)
            suspendF("mysql", 1)
        }
        Thread.sleep(300000)
    }

    @Test
    fun `병렬적으로 실행됨`() {
        GlobalScope.launch {
            launch { suspendF("1", 1) }
            launch { suspendF("2", 1) }
        }
        Thread.sleep(5000)
    }

    @Test
    fun `순차적으로 실행됨?`() {
        runBlocking {
//            withContext(Dispatchers.IO) {
//                asyncSuspendF("1", 1)
//                asyncSuspendF("2", 1)
//            }

            withContext(Dispatchers.IO) {
                val d1 = launch { suspendF("1", 1) }
                val d2 = launch { suspendF("2", 1)  }
                d1.join()
                d2.join()
            }

            CoroutineScope(Dispatchers.IO).launch { asyncSuspendF("3", 1) }
        }
    }


    @Test
    fun `coroutineScope test`() {
        return runBlocking(newSingleThreadContext("run")) {
            printlnWithThread("runBlocking before")

            coroutineScope { // 현재 context와 동일한 context로 스코프 생성
                printlnWithThread("coroutineScope")
                launch { suspendF("redis", 1) }
                launch { suspendF("mysql", 3) }
            }

            withContext(this.coroutineContext) { // coroutineScope와 동일
                printlnWithThread("withContext")
            }

            // 새로운 context로 scope 생성
            CoroutineScope(Dispatchers.IO).launch {
                printlnWithThread("IO")
                launch { suspendF("client", 1) }
            }.join()

            printlnWithThread("runBlocking after")
        }
    }


    @Test
    fun `async await test`() {
        runBlocking {
            printlnWithThread("runBlocking start")
            (1..10)
                .chunked(2)
                .mapIndexed { index, it ->
                    printlnWithThread("chunked $index start")
                    it.map {
                        async {
                            delay(1000)
                            printlnWithThread(it)
                            it * 2
                        }
                    }
                        .awaitAll()
                        .also {
                            printlnWithThread(it)
                            printlnWithThread("chunked $index end")
                        }
                }
                .flatten()
            printlnWithThread("runBlocking end")
        }
    }

    @Test
    fun `newFixedThreadPoolContext test`() {
        val context = newFixedThreadPoolContext(1, "jdbc")

        return runBlocking {
            measureTimeMillis {
                (0..50000)
                    .map {
                        async {
                            withContext(context) {
                                delay(1000)
                                printlnWithThread(it)
                                it * 2
                            }
                        }
                    }.awaitAll()
            }.let { printlnWithThread(it) }
        }
    }

    @Test
    fun `누가누가 먼저 실행되나 1`() { // 결과 동시에 실행된다
        return runBlocking {
            measureTimeMillis {
                val asyncDeffered = async { suspendF("async", 3) }
                val launchJob = launch { suspendF("launch", 2) }.join()
                asyncDeffered.await()
            }.let { printlnWithThread(it) }
        }
    }

    @Test
    fun `누가누가 먼저 실행되나 2`() {
        return runBlocking { // 결과 launch부터 실행되고 블락킹됨 join을 만나서 그런듯!
            measureTimeMillis {
                launch { suspendF("launch", 2) }.join()
                async { suspendF("async", 1) }.await()
            }.let { printlnWithThread(it) }
        }
    }

    @Test
    fun `하나가 실패하면?`() { // 결과 같은 scope의 coroutine도 최소됨
        return runBlocking {
            measureTimeMillis {
                val launchJob = launch { suspendF("launch", 1); throw RuntimeException() }
                val asyncDeffered = async { suspendF("async", 2) }
                launchJob.join() to asyncDeffered.await()
            }.let { printlnWithThread(it) }
        }
    }

    @Test
    fun `runBloking 내부 coroutine도 일시중단 가능하다`() {
        runBlocking { // (1)
            launch { // (2)
                printWorld() // (3)
            }
//            printWorld()
            printlnWithThread("Hello") // (6)
        }
    }

    suspend fun printWorld() { // (4)
        delay(1000L) // (5)
        printlnWithThread("World!") // (7)
    }


    @Test
    fun `blocking test`() {
        runBlocking {
            withContext(Dispatchers.IO) {
                printlnWithThread("withContext")
                launch { suspendF("1", 1) }
                launch { suspendF("2", 1) }
            }
        }
        printlnWithThread("====")
        runBlocking {
            withContext(Dispatchers.IO) {
                printlnWithThread("withContext")
                launch(Dispatchers.IO) { suspendF("1", 1) }
                launch(Dispatchers.IO) { suspendF("2", 1) }
            }
        }

    }


    private suspend fun suspendF(name: String, delaySecond: Int) {
        printlnWithThread("$name start")
        delay(delaySecond * 1000L)
        printlnWithThread("$name end")
    }

    private suspend fun asyncSuspendF(name: String, delaySecond: Int) {
        coroutineScope {
            launch { suspendF(name, delaySecond) }
        }
    }
}