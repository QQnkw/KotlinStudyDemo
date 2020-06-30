import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 挂起函数的组合
 */
class M1 {
    /**
     * 打印
     * The answer is 42
     * Completed in 2017 ms
     */
    fun aa() = runBlocking<Unit> {
        //sampleStart
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")
        //sampleEnd
    }
/*-------------------------------------async--------------------------------*/
    /**
     * 使用async
     * 打印
     * The answer is 42
     * Completed in 1017 ms
     * 执行速度快了 2 倍, 因为我们让两个协程并发执行. 注意, 协程的并发总是需要明确指定的.
     *
     * 概念上来说, async 就好象 launch 一样.
     * 它启动一个独立的协程, 也就是一个轻量的线程, 与其他所有协程一起并发执行.
     * 区别在于, launch 返回一个 Job, 其中不带有结果值,
     * 而 async 返回一个 Deferred – 一个轻量的, 非阻塞的 future,
     * 代表一个未来某个时刻可以得到的结果值.
     * 你可以对一个延期值(deferred value)使用 .await() 来得到它最终的计算结果,
     * 但 Deferred 同时也是一个 Job, 因此如果需要的话, 你可以取消它.
     */
    fun bb() = runBlocking {
        //sampleStart
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
        //sampleEnd
    }
/*-----------------------------延迟启动的(Lazily started) async---------------------------------*/
    /**
     * 将可选的 start 参数设置为 CoroutineStart.LAZY,
     * 可以让 async 延迟启动. 只有在通过 await 访问协程的计算结果的时候,
     * 或者调用 start 函数的时候, 才会真正启动协程.
     * 打印
     * The answer is 42
     * Completed in 1017 ms
     *
     * 注意, 如果我们在 println 内调用 await,
     * 而省略了对各个协程的 start 调用, 那么两个协程的执行结果将会是连续的,
     * 而不是并行的, 因为 await 会启动协程并一直等待执行结束,
     * 这并不是我们使用延迟加载功能时期望的效果.
     * 如果计算中使用到的值来自挂起函数的话,
     * 可以使用 async(start = CoroutineStart.LAZY) 来代替标准的 lazy 函数.
     */
    fun cc() = runBlocking<Unit> {
        //sampleStart
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            // 执行某些计算
            one.start() // 启动第一个协程
            two.start() // 启动第二个协程
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
        //sampleEnd
    }

    /*------------------------------async 风格的函数-------------------------------*/
    /**
     * 我们可以定义一个 async 风格的函数,
     * 它使用一个明确的 GlobalScope 引用,
     * 通过 async 协程构建器来 异步地 调用 doSomethingUsefulOne 和 doSomethingUsefulTwo.
     * 我们将这类函数的名称加上 “Async” 后缀,
     * 明确表示这些函数只负责启动异步的计算工作,
     * 函数的使用者需要通过函数返回的延期值(deferred value)来得到计算结果.
     *
     * 注意, 这些 xxxAsync 函数 不是 挂起 函数.
     * 这些函数可以在任何地方使用.
     * 但是, 使用这些函数总是会隐含着异步执行(这里的意思是 并发)它内部的动作.
     */
    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
    fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }
/*------------------------------使用 async 的结构化并发-------------------------------*/
    /**
     *抽取一个函数, 并发地执行 doSomethingUsefulOne 和 doSomethingUsefulTwo,
     * 并返回这两个函数结果的和. 由于 async 协程构建器被定义为 CoroutineScope 上的扩展函数,
     * 因此我们使用这个函数时就需要在作用范围内存在 CoroutineScope,
     * coroutineScope 函数可以为我们提供 CoroutineScope;
     *
     * 通过这种方式, 如果 concurrentSum 函数内的某个地方发生错误, 抛出一个异常,
     * 那么在这个函数的作用范围内启动的所有协程都会被取消.
     */
    suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    /**
     *打印
     * The answer is 42
     * Completed in 1017 ms
     */
    fun dd() = runBlocking<Unit> {
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了某些有用的工作
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了某些有用的工作
        return 29
    }

    /**
     * 通过协程的父子层级关系, 取消总是会层层传递到所有的子协程, 以及子协程的子协程;
     * 注意, 当一个子协程失败时, 第一个 async, 以及等待子协程的父协程都会被取消;
     * 打印:
     * Second child throws an exception
     * First child was cancelled
     * Computation failed with ArithmeticException
     */
    fun ee() = runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }

    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的计算过程
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }
}