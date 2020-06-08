open class AAA {
    open fun f() {
        println("AAA:F")
    }

    fun a() {
        println("AAA:A")
    }

    open fun c() {
        println("AAA:C")
    }

    open fun d(d: Int) {
        println("AAA:D")
    }

    fun e() {
        println("AAA:E")
    }
}

interface BBB {
    fun f() {
        println("BBB:F")
    } // 接口的成员默认是 'open' 的

    fun b() {
        println("BBB:B")
    }

    fun c(string: String) {
        println("BBB:C")
    }

    fun d(string: String) {
        println("BBB:D")
    }

    fun e()/*{
        println("BBB:E")
    }*///打开注释会报错
}

/**
 * 多继承时,不同父类中有同名,参数个数相同,参数类型一致的且都有方法实体的时候:
 * 子类必须覆盖这个成员,
 * 并提供一个自己的实现(可以使用继承得到的多个实现中的某一个).
 * 为了表示使用的方法是从哪个超类继承得到的, 我们使用 super 关键字, 将超类名称放在尖括号类,
 *
 *  CCC类从超类中继承得到了两个实现, 因此在CCC类中必须覆盖函数 f(), 并提供我们自己的实现, 这样才能消除歧义.
 */
class CCC() : AAA(), BBB {
    //编译器要求 f() 方法必须覆盖:
    override fun f() {
        super<AAA>.f() // 调用 A.f()
        super<BBB>.f() // 调用 B.f()
        println("CCC:F")
    }

    override fun c() {
        super<AAA>.c()
        println("CCC:C")
    }

    override fun c(string: String) {
        super<BBB>.c(string)
        println("CCC:C-")
    }

    override fun d(d: Int) {
        super<AAA>.d(d)
        println("CCC:D")
    }

    override fun d(string: String) {
        super<BBB>.d(string)
        println("CCC:D-")
    }
}

/**
 *类本身, 或类中的部分成员, 都可以声明为 abstract 的.
 * 抽象成员在类中不存在具体的实现.
 * 注意, 我们不必对抽象类或抽象成员标注 open 修饰符 – 因为它显然必须是 open 的.
 */
open class D {
    open fun f() {}
}

abstract class DDD : D() {
    override abstract fun f()
}
