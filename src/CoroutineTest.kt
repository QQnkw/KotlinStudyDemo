import javafx.application.Application.launch
import kotlinx.coroutines.*
import javax.xml.bind.JAXBElement

class K1 {
    /** delay 是一个特殊的 挂起函数(suspending function),
     * 它不会阻塞进程, 但会 挂起 协程, 因此它只能在协程中使用.
     * */
    /**
     * 打印
     * Hello,
     * World!
     */
    fun aa() {
        GlobalScope.launch { // 在后台启动新的协程, 然后继续执行当前程序
            delay(1000L) // 非阻塞, 等待 1 秒 (默认的时间单位是毫秒)
            println("World!") // 等待完成后打印信息
        }
        println("Hello,") // 当协程在后台等待时, 主线程继续执行
        Thread.sleep(2000L) // 阻塞主线程 2 秒, 保证 JVM 继续存在
    }

    /**
     * 打印
     * Hello,
     * World!
     * 这段代码只使用非阻塞的 delay 函数.
     * 在主线程中, 调用 runBlocking 会 阻塞,
     * 直到 runBlocking 内部的协程执行完毕.
     */
    fun bb() {
        GlobalScope.launch { // 在后台启动新的协程, 然后继续执行当前程序
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程在这里立即继续执行
        runBlocking {     // 但这个表达式会阻塞主线程
            delay(2000L)  // ... 我们在这里等待 2 秒, 保证 JVM 继续存在
        }
    }

    /**
     * 用更符合 Kotlin 语言编程习惯的方式重写这个示例程序,
     * 用 runBlocking 来包装主函数的运行;
     *
     * 这里 runBlocking<Unit> { ... } 起一种适配器的作用, 用来启动最上层的主协程.
     * 我们明确指定了返回值类型为 Unit,
     * 因为 Kotlin 语言中, 语法正确的 main 函数必须返回 Unit.
     */
    fun bbb() = runBlocking<Unit> { // 启动主协程
        GlobalScope.launch { // 在后台启动新的协程, 然后继续执行当前程序
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里立即继续执行
        delay(2000L)      // 等待 2 秒, 保证 JVM 继续存在
    }

    /*----------------------------------等待一个任务完成-----------------------------------*/
    /**
     * 当其他协程正在工作时, 等待一段固定的时间, 这是一种不太好的方案.
     * 下面我们(以非阻塞的方式)明确地等待我们启动的后台 Job 执行完毕;
     */
    fun bc() = runBlocking {
        //sampleStart
        val job = GlobalScope.launch { // 启动新的协程, 并保存它的执行任务的引用
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        job.join() // 等待, 直到子协程执行完毕
        //sampleEnd
    }
/*----------------------------------结构化的并发-----------------------------------*/
    /**
     * 有一个 bbcc 函数, 它使用 runBlocking 协程构建器变换成了一个协程.
     * 所有的协程构建器, 包括 runBlocking,
     * 都会向它的代码段的作用范围添加一个 CoroutineScope 的实例.
     * 我们在这个作用范围内启动协程,
     * 而不需要明确地 join 它们,
     * 因为外层协程 (在我们的示例程序中就是 runBlocking)
     * 会等待它的作用范围内启动的所有协程全部完成.
     * 因此, 我们可以把示例程序写得更简单一些:
     */
    fun bbcc() = runBlocking { // this: CoroutineScope
        launch { // 在 runBlocking 的作用范围内启动新的协程
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }

    /*----------------------------------作用范围(Scope)构建器-----------------------------------*/
    /**
     * 除了各种构建器提供的协程作用范围之外,
     * 还可以使用 coroutineScope 构建器来自行声明作用范围.
     * 这个构建器可以创建新的协程作用范围,
     * 并等待在这个范围内启动的所有子协程运行结束.
     * runBlocking 和 coroutineScope 之间的主要区别是,
     * coroutineScope 在等待子协程运行时, 不会阻塞当前线程.
     */
    fun bbbccc() = runBlocking { // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope { // 创建新的协程作用范围
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 在嵌套的 launch 之前, 这一行会打印
        }

        println("Coroutine scope is over") // 直到嵌套的 launch 运行结束后, 这一行才会打印
    }
    /*----------------------------------抽取函数(Extract Function)的重构-----------------------------------*/
    /**
     * 打印
     * Hello,
     * World!
     * 简化代码如下:
     */
    fun cc() = runBlocking { // this: CoroutineScope
        launch { // 在 runBlocking 的作用范围内启动新的协程
            dd()
        }
        println("Hello,")
    }

    /**
     * 带 suspend 修饰符的新函数. 这就是一个 挂起函数.
     * 在协程内部可以象使用普通函数那样使用挂起函数,
     * 但挂起函数与普通函数的不同在于,
     * 它们又可以使用其他挂起函数,
     * 比如下面的例子中, 我们使用了 delay 函数, 来 挂起 当前协程的运行.
     */
    suspend fun dd() {
        delay(1000L)
        println("World!")
    }
/*-----------------------------------------*/
    /**
     * 全局协程类似于守护线程
     * I'm sleeping 0 ...
     * I'm sleeping 1 ...
     * I'm sleeping 2 ...
     * 打印 3 行消息, 然后就结束了
     * 在 GlobalScope 作用范围内启动的活跃的协程,
     * 不会保持应用程序的整个进程存活. 它们的行为就象守护线程一样.
     */
    fun ee() {
        runBlocking {
            GlobalScope.launch {
                repeat(1000) {
                    println("I'm sleeping $it ...")
                    delay(500L)
                }
            }
            delay(1300L)
        }
    }
/*-----------------取消协程的运行------------------------*/
    /**
     * 协程的执行可以取消.
     * launch 函数会返回一个 Job,
     * 可以通过它来取消正在运行的协程;
     *
     * 一旦 main 函数调用 job.cancel,
     * 我们就再也看不到协程的输出了,
     * 因为协程已经被取消了.
     * 还有一个 Job 上的扩展函数 cancelAndJoin,
     * 它组合了 cancel 和 join 两个操作.
     */
    fun ff() {
        runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancel() // 取消 job
            job.join() // 等待 job 结束
            println("main: Now I can quit.")
        }
    }
/*-----------------取消是协作式的------------------------*/
    /**
     *一个协程正在进行计算, 并且没有检查取消状态, 那么它是不可被取消的,
     * 运行一下这个示例,即使在取消之后, 协程还是继续打印 “I’m sleeping” 信息,
     * 直到循环 5 次之后, 协程才自己结束.
     */
    fun gg() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // 一个浪费 CPU 的计算任务循环
                    // 每秒打印信息 2 次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消 job, 并等待它结束
            println("main: Now I can quit.")
        }
    }

    /*-----------------使计算代码能够被取消------------------------*/
    /**
     * 有两种方法可以让我们的计算代码变得能够被取消.
     * 第一种办法是定期调用一个挂起函数, 检查协程是否被取消. 有一个 yield 函数可以用来实现这个目的.
     * 另一种方法是显式地检查协程的取消状态.
     * 来试试后一种方法:
     *  现在循环变得能够被取消了.
     *  isActive 是一个扩展属性,
     *  在协程内部的代码中可以通过 CoroutineScope 对象访问到.
     */
    fun hh() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) { // 可被取消的计算循环
                    // 每秒打印信息 2 次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消 job, 并等待它结束
            println("main: Now I can quit.")
        }
    }

    /*-----------------使用 finally 语句来关闭资源------------------------*/
    /**
     * 可被取消的挂起函数, 在被取消时会抛出 CancellationException 异常,
     * 这个异常可以通过通常方式来处理.
     * 比如, 可以使用 try {...} finally {...} 表达式,
     * 或者 Kotlin 的 use 函数, 以便协程被取消时来执行结束处理:
     * join 和 cancelAndJoin 都会等待所有的结束处理执行完毕, 因此上面的示例程序会产生这样的输出:
     * I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    main: I'm tired of waiting!
    I'm running finally
    main: Now I can quit.
     */
    fun ii() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    println("I'm running finally")
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消 job, 并等待它结束
            println("main: Now I can quit.")
        }
    }

    /*-----------------运行无法取消的代码段------------------------*/
    /**
     * 如果试图在上面示例程序的 finally 代码段中使用挂起函数,
     * 会导致 CancellationException 异常,
     * 因为执行这段代码的协程已被取消了.
     * 通常, 这不是问题, 因为所有正常的资源关闭操作
     * (关闭文件, 取消任务, 或者关闭任何类型的通信通道) 通常都是非阻塞的,
     * 而且不需要用到任何挂起函数.
     * 但是, 在极少数情况下,
     * 如果你需要在已被取消的协程中执行挂起操作,
     * 你可以使用 withContext 函数和 NonCancellable 上下文,
     * 把相应的代码包装在 withContext(NonCancellable) {...} 内
     */
    fun jj() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("I'm running finally")
                        delay(1000L)
                        println("And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // 等待一段时间
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // 取消 job, 并等待它结束
            println("main: Now I can quit.")
        }
    }
    /*-----------------超时------------------------*/
    /**
     * 在实际应用中, 取消一个协程最明显的理由就是, 它的运行时间超过了某个时间限制.
     * 当然, 你可以手动追踪协程对应的 Job, 然后启动另一个协程,
     * 在等待一段时间之后取消你追踪的那个协程,
     * 但 Kotlin 已经提供了一个 withTimeout 函数来完成这个任务.
     * I'm sleeping 0 ...
     *I'm sleeping 1 ...
     *I'm sleeping 2 ...
     *Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
     * withTimeout 函数抛出的 TimeoutCancellationException 异常
     * 是 CancellationException 的子类.
     * 我们在前面的例子中, 都没有看到过 CancellationException
     * 异常的调用栈被输出到控制台. 这是因为,
     * 在被取消的协程中 CancellationException 被认为是协程结束的一个正常原因.
     * 但是, 在这个例子中我们直接在 main 函数内使用了 withTimeout.
     */
    fun kk() {
        runBlocking {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        }
    }

    /**
     * 由于协程的取消只是一个异常, 因此所有的资源都可以通过通常的方式来关闭.
     * 如果你需要在超时发生时执行一些额外的操作, 可以将带有超时控制的代码封装在
     * try {...} catch (e: TimeoutCancellationException) {...}
     * 代码块中, 也可以使用 withTimeoutOrNull 函数,
     * 它与 withTimeout 函数类似, 但在超时发生时, 它会返回 null, 而不是抛出异常:
     * I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    Result is null
     */
    fun ll() {
        runBlocking {
            val result = withTimeoutOrNull(1300L) {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
                "Done" // 协程会在输出这个消息之前被取消
            }
            println("Result is $result")
        }
    }
}