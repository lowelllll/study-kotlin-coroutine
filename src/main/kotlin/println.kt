

fun <T> printlnWithThread(any: T) {
    println("[${Thread.currentThread().name}] $any")
}