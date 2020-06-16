import kotlin.properties.Delegates

/*-----------------委托属性--------------------*/
/**
 * 有许多非常具有共性的属性,
 * 虽然我们可以在每个需要这些属性的类中手工地实现它们,
 * 但是, 如果能够只实现一次, 然后将它放在库中, 供所有需要的类使用, 那将会好很多. 这样的例子包括:
 * 1.延迟加载属性(lazy property): 属性值只在初次访问时才会计算;
 * 2.可观察属性(observable property): 属性发生变化时, 可以向监听器发送通知;
 * 3.将多个属性保存在一个 map 内, 而不是将每个属性保存在一个独立的域内.
 */
/**
 * 延迟加载
 *
 * 默认情况下, 延迟加载属性(lazy property)的计算是 同步的(synchronized):
 * 属性值只会在唯一一个线程内计算, 然后所有线程都将得到同样的属性值.
 * 如果委托的初始化计算不需要同步, 多个线程可以同时执行初始化计算,
 * 那么可以向lazy() 函数传入一个 LazyThreadSafetyMode.PUBLICATION 参数.
 * 相反, 如果你确信初期化计算只可能发生在一个线程内, 那么可以使用 LazyThreadSafetyMode.NONE 模式,
 * 这种模式不会保持线程同步, 因此不会带来这方面的性能损失.
 */

class J1 {
    val a: String by lazy {
        println("lazy")
        "hello"
    }
}

/**
 *可观察属性
 *
 * Delegates.observable() 函数接受两个参数:
 * 第一个是初始化值, 第二个是属性值变化事件的响应器(handler).
 * 每次我们向属性赋值时, 响应器(handler)都会被调用(在属性赋值处理完成 之后).
 * 响应器收到三个参数: 被赋值的属性, 赋值前的旧属性值, 以及赋值后的新属性值;
 * 如果你希望能够拦截属性的赋值操作, 并且还能够 “否决” 赋值操作,
 * 那么不要使用 observable() 函数, 而应该改用 vetoable() 函数.
 * 传递给 vetoable 函数的事件响应器, 会在属性赋值处理执行 之前 被调用.
 */
class J3 {
    var a: String by Delegates.observable("no") { property, oldValue, newValue ->
        println("$oldValue->$newValue")
    }
    var b: String by Delegates.vetoable("yes") { property, oldValue, newValue ->
        newValue == "111"
    }
}

/**
 * 将多个属性保存在一个 map 内
 *
 * 如果不用只读的 Map, 而改用值可变的 MutableMap,
 * 那么也可以用作 var 属性的委托:
 */
class J4(val map: Map<String, Any?>) {
    val a: String by map
    val b: Int by map
}

class J2 {
    fun a() {
        //延迟加载
//        val j1 = J1()
//        println(j1.a)//打印lazy hello
//        println(j1.a)//打印hello
        //可观察属性
//        val j3 = J3()
//        j3.a = "123"//打印no->123
//        j3.a = "456"//打印123->456
        /*-----------*/
//        j3.b = "111"
//        println(j3.b)//打印111,属性更改
//        j3.b = "222"
//        println(j3.b)//打印111,属性未更改成功
        //将多个属性保存在一个 map 内
        val j4 = J4(mapOf(
                "a" to "123",
                "b" to 456
        ))
        println(j4.a)//打印123
        println(j4.b)//打印456
    }
}