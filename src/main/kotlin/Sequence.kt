

val fibonacci = sequence {
    var a = 0
    var b = 1
    yield(1)

    while (true) {
        yield(a + b) // 실행을 잠시 멈추고 요소를 반환함 (값을 산출)
        val tmp = a + b
        a = b
        b = tmp
    }
}

val fibonacci2 = sequence {
    val start = 0
    yield(start)
    yieldAll(1..5 step 2)
    yieldAll(generateSequence(8) { it * 3 }) // 무한한 시퀀스
}

fun main() {
    println(fibonacci.take(8).toList())
    println(fibonacci2.take(7).toList())
}