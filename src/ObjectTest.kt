/*-----------------------------对象表达式--------------------------------*/
/**
 * 匿名内部类声明
 */
open class H1(a: Int) {
    public open val b: Int = a
}

interface H2

class H3 {
    val c: H1 = object : H1(1), H2 {
        override val b: Int = 2
    }

    /**
     * 如果,“只需要对象”,
     * 而不需要继承任何有价值的基类,
     * 可以简单地写:
     */
    fun a() {
        val c = object {
            var d: Int = 3
            var e: Int = 4
        }
        println(c.d + c.e)
    }
}

/**
 * 注意, 只有在局部并且私有的声明范围内, 匿名对象才可以被用作类型.
 * 如果你将匿名对象用作公开函数的返回类型, 或者用作公开属性的类型,
 * 那么这个函数或属性的真实类型会被声明为这个匿名对象的超类,
 * 如果匿名对象没有超类, 则是 Any.
 * 在匿名对象中添加的成员将无法访问.
 */
class H4 {
    // 私有函数, 因此它的返回类型为匿名对象类型
    private fun a() = object {
        val a1: String = "a"
    }

    // 公开函数, 因此它的返回类型为 Any
    fun publicB() = object {
        val b1: String = "b"
    }

    val d = object : H1(1) {
        val d1 = 10
        var d2 = 20
    }

    fun c() {
        val c1 = a().a1        // 正确
//        val c2 = publicB().b1  // 错误: 无法找到 'b1'
//        val c3 = d.d1// 错误: 无法找到 'd1'
    }
}
/*-----------------------------对象声明--------------------------------*/
// 1.对象声明中的初始化处理是线程安全的,
// 2.对象也可以指定基类,
// 3.对象声明不可以是局部的(也就是说, 不可以直接嵌套在函数之内),
// 但可以嵌套在另一个对象声明之内, 或者嵌套在另一个非内部类(non-inner class)之内.
/**
 * 单例模式
 */
object H5 : H7 {
    var a: Int = 1
    fun b() {

    }
}

interface H7
class H6 {
    fun a() {
        H5.b()
        H5.a
    }
}
////////////////////////////////////////////////////////////
/**
 * 同伴对象
 */
class H8 {
    companion object {
        fun a() = 1
        val b: Int = 2
    }

    fun a() {
        val a1 = H9.a
        val a2 = H9.b()
        val a3 = H9.Factory.a
        val a4 = H9.Factory.b()
    }
}

class H9 {
    fun a() {
        /**
         * a1等同于a5
         */
        val a1 = H8.b
        val a2 = H8.a()
        val a3 = a
        val a4 = b()
        val a5 = H8.Companion.b
        val a6 = H8.Companion.a()

    }

    companion object Factory {
        val a = 1
        fun b() = 2
    }
}

/**
 * 直接使用一个类的名称时 (而不是将它用作另一个名称前面的限定符)
 * 会被看作是这个类的同伴对象的引用 (无论同伴对象有没有名称);
 * 同伴对象还可以实现接口
 */
interface H10<out T> {
    fun a(): T
}

class H11 {
    companion object : H10<H11> {
        override fun a(): H11 = H11()
    }
}

class H12 {
    fun a() {
        val a1: H10<H11> = H11
    }
}
/**
 * 对象表达式与对象声明在语义上的区别
 * 对象表达式与对象声明在语义上存在一个重要的区别:
 * 1.对象表达式则会在使用处 立即 执行(并且初始化);
 * 2.对象声明是 延迟(lazily) 初始化的, 只会在首次访问时才会初始化;
 * 3.同伴对象会在对应的类被装载(解析)时初始化, 语义上等价于 Java 的静态初始化代码块(static initializer).
 */