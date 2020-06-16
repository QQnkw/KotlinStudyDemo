/*------------------------通过委托实现接口--------------------------*/
interface I1 {
    fun countNum()
    fun sum()
    val message: String
    fun printMessage()
}

class I1Impl() : I1 {
    override fun countNum() {
        println("countNum")
    }

    override fun sum() {
        println("sum:I1Impl")
    }

    override val message: String = "I1Impl"
    override fun printMessage() {
        println(message)
    }
}

class I2(i: I1) : I1 by i {
    override fun sum() {
        println("sum:I2")
    }

    override val message: String = "I2"
}

class I3 {
    fun a() {
        val i = I1Impl()
        val i2 = I2(i)
        i2.countNum()//打印countNum;调用的I1Impl的方法
        i2.sum()//sum:I2;调用I2重写的方法
        /**
         * 注意, 使用上述方式覆盖的接口成员,
         * 在委托对象的成员函数内无法调用.
         * 委托对象的成员函数内, 只能访问它自己的接口方法实现:
         */
        i2.printMessage()//打印I1Impl
        println(i2.message)//打印I2
    }
}