open class BaseB {
    open val a: Int = 1
    open val b: Int
        get() {
            return 2
        }

}

class B : BaseB() {
    override val a: Int = 11
    override val b: Int
        get() {
            return 22
        }
}

/**
 * 可以使用一个 var 属性覆盖一个 val 属性, 但不可以反过来使用一个 val 属性覆盖一个 var 属性.
 * 允许这种覆盖的原因是, val 属性本质上只是定义了一个 get 方法, 使用 var 属性来覆盖它,
 * 只是向后代类中添加了一个 set 方法.
 */
interface InterfaceB {
    val a: Int
}

class Impl1(override val a: Int) : InterfaceB
class Impl2 : InterfaceB {
    override var a: Int = 1
}

/**
 * 在内部类(inner class)的代码中,
 * 可以使用 super 关键字加上外部类名称限定符: super@Outer 来访问外部类(outer class)的超类:
 */
open class Foo {
    open fun f() {
        println("Foo.f()")
    }

    open val x: Int get() = 1
}

class Bar : Foo() {
    override fun f() { /* ... */
    }

    override val x: Int get() = 0

    inner class Baz {
        fun g() {
            super@Bar.f() // 调用 Foo 类中的 f() 函数实现
            println(super@Bar.x) // 使用 Foo 类中的 x 属性取值方法实现
        }
    }
}