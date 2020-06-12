interface F {
    val a: Int
    val b: Int
        get() = 111

    fun aa() {
        println("aa")
    }

    var c: Int
}

/**
 * 接口的实现
 */
class FImpl : F {
    override val a: Int = 222
    override var c: Int = 333
}

/**
 * 接口的继承
 */
interface FF : F {
    override val a: Int
        get() = 5555
    val d: Int
}

class FFImpl(override var c: Int, override val d: Int) : FF