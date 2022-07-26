import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

// async, launch의 차이점이 ..
// async: Deffered<T> 반환
// launch: Job 반환

fun main() = runBlocking {
    val jobs = arrayListOf<Job>()
    jobs += launch(Dispatchers.Unconfined) {  // main thread
        // 사용 지양
        println("Unconfined: ${Thread.currentThread().name}")
    }

    jobs += launch(coroutineContext) { // 부모 (runBlocking)의 문맥
        println("coroutineContext: ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.Default) { // 디스패처 기본값
        // 공유된 배그라운드 스레드의 CommonPool에서 코루틴을 실행하도록 함.
        // 스레드를 생성하지 않고 기존에 있는 것을 이용함.
        // 연산 중심의 코드에 적합함.
        // GlobalScope.launch와 동일 
        println("default: ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.IO) { // 입출력 중심 문맥
        // 입출력 위주의 동작을 하는 코드에 적합한 공유된 풀
        println("IO: ${Thread.currentThread().name}")
    }

    jobs += launch {
        println("main runBlocking: ${Thread.currentThread().name}")
    }

    jobs += launch(newSingleThreadContext("my thread")) { // 새 스래드 생성
        println("my thread: ${Thread.currentThread().name}")
    }



    jobs.forEach { it.join() }
}