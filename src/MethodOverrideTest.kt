open class BaseA {
    open fun getName() {

    }

    fun getAge() {

    }
}

/**
 * 当一个子类成员标记了 override 修饰符来覆盖父类成员时, 覆盖后的子类成员本身也将是 open 的,
 * 也就是说, 子类成员可以被自己的子类再次覆盖. 如果你希望禁止这种再次覆盖, 可以使用 final 关键字:
 */
open class A : BaseA() {
    override fun getName() {
//    final override fun getName() {
        println("A:getName")
    }
}

class AA : A() {
    override fun getName() {
        println("AA:getName")
    }
}
