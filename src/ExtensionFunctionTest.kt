/**
 * 扩展函数
 */
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' 指代 list 实例
    this[index1] = this[index2]
    this[index2] = tmp
}
/*-------------------------------------------*/
/**
 * printFoo(G2())最终打印c
 */
open class G1

class G2 : G1()

fun G1.foo() = "c"

fun G2.foo() = "d"

fun printFoo(g: G1) {
    println(g.foo())
}
/*-------------------------------------------*/
/**
 * G3().foo()打印member
 */
class G3 {
    fun foo() {
        println("member")
    }
}

fun G3.foo() {
    println("extension")
}
/*-------------------------------------------*/
/**
 * println(G4(1))打印为extension
 */
class G4 {
    fun foo() {
        println("member")
    }
}

fun G4.foo(i: Int) {
    println("extension")
}
/*-------------------------------------------*/
/**
 * 对象是否为 null 的检查发生在扩展函数内部, 因此调用者不必再做检查.
 */
fun Any?.toString(): String {
    if (this == null) return "null"
    // 进行过 null 检查后, 'this' 会被自动转换为非 null 类型, 因此下面的 toString() 方法
    // 会被解析为 Any 类的成员函数
    return toString()
}
/*-------------------------------------------*/
/**
 * 扩展属性(Extension Property)
 *与扩展函数类似, Kotlin 也支持扩展属性:
 */

val <T> List<T>.lastIndex: Int
    get() = size - 1
/**
 * 注意, 由于扩展属性实际上不会向类添加新的成员, 因此无法让一个扩展属性拥有一个 后端域变量.
 * 所以, 对于扩展属性不允许存在初始化器. 扩展属性的行为只能通过明确给定的取值方法与设值方法来定义.
 */
//val G4.bar = 1 // 错误: 扩展属性不允许存在初始化器
/*-------------------------------------------*/
/**
 * 对同伴对象(Companion Object)的扩展
 * 如果一个类定义了同伴对象, 你可以对这个同伴对象定义扩展函数和扩展属性:
 */
class G5 {
    companion object {}  // 通过 "Companion" 来引用这个同伴对象
}

fun G5.Companion.foo() {}
//与同伴对象的常规成员一样, 可以只使用类名限定符来调用这些扩展函数和扩展属性:
//G5.foo()
/*-------------------------------------------*/
/**
 *将扩展定义为成员
 * 在类的内部, 你可以为另一个类定义扩展.
 * 在这类扩展中, 存在多个 隐含接受者(implicit receiver)
 * - 这些隐含接收者的成员可以不使用限定符直接访问.
 * 扩展方法的定义所在的类的实例, 称为_派发接受者(dispatch receiver),
 * 扩展方法的目标类型的实例, 称为 _扩展接受者(extension receiver).
 */
class G6 {
    fun bar() {}
}

class G7 {
    fun baz() {}

    fun G6.foo() {
        bar()   // 这里将会调用 G6.bar
        baz()   // 这里将会调用 G7.baz
    }

    fun caller(d: G6) {
        d.foo()   // 这里将会调用扩展函数
    }
}

/**
 * 当派发接受者与扩展接受者的成员名称发生冲突时, 扩展接受者的成员将会被优先使用.
 * 如果想要使用派发接受者的成员, 请参见 带限定符的 this 语法.
 */
class G8 {
    fun G7.foo() {
        toString()         // 这里将会调用 G7.toString()
        this@G8.toString()  // 这里将会调用 G8.toString()
    }
}

/**
 * 以成员的形式定义的扩展函数, 可以声明为 open,而且可以在子类中覆盖.
 * 也就是说, 在这类扩展函数的派发过程中,
 * 针对派发接受者是虚拟的(virtual), 但针对扩展接受者仍然是静态的(static).
 */
open class G9 {}

class G10 : G9() {}

open class G11 {
    open fun G9.foo() {
        println("D.foo in C")
    }

    open fun G10.foo() {
        println("D1.foo in C")
    }

    fun caller(d: G9) {
        d.foo()   // 调用扩展函数
    }
}

class G12 : G11() {
    override fun G9.foo() {
        println("D.foo in C1")
    }

    override fun G10.foo() {
        println("D1.foo in C1")
    }
}

fun main() {
    G11().caller(G9())   // 打印结果为 "D.foo in C"
    G12().caller(G9())  // 打印结果为 "D.foo in C1" - 派发接受者的解析过程是虚拟的
    G11().caller(G10())  // 打印结果为 "D.foo in C" - 扩展接受者的解析过程是静态的
}
/*-------------------------------------------*/
/**关于可见度的注意事项
 * 扩展函数或扩展属性 对其他元素的可见度 规则, 与定义在同一范围内的普通函数相同. 比如:
 * 定义在源代码文件顶级(top-level)范围内的扩展, 可以访问同一源代码文件内的其他顶级 private 元素;
 * 如果扩展定义在它的接受者类型的外部, 那么这样的扩展不能访问接受者的 private 成员.*/
/*-------------------------------------------*/
/**
 * 使用扩展的动机
 * 在 Java 中, 我们通常会使用各种名为 “*Utils” 的工具类: FileUtils, StringUtils 等等.
 * 著名的 java.util.Collections 也属于这种工具类.
 * 这种工具类模式令人很不愉快的地方在于, 使用时代码会写成这种样子:
 * // Java
 * Collections.swap(list, Collections.binarySearch(list,Collections.max(otherList)),Collections.max(list));
 * 代码中反复出现的工具类类名非常烦人. 我们也可以使用静态导入(tatic import), 然后代码会变成这样:
 * // Java
 * swap(list, binarySearch(list, max(otherList)), max(list));
 *
 * 这样略好了一点点, 但是没有了类名做前缀,
 * 就导致我们无法利用 IDE 强大的代码自动补完功能. 如果我们能写下面这样的代码, 那不是很好吗:
 * // Java
 * list.swap(list.binarySearch(otherList.max()), list.max());
 * 但是我们又不希望将一切可能出现的方法在 List 类之内全部都实现出来, 对不对? 这恰恰就是 Kotlin 的扩展机制可以帮助我们解决的问题.
 */


