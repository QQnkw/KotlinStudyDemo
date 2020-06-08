import java.lang.Integer.parseInt

fun main() {
    println("开始")
//    test1()
//    test2()
//    test3()
//    test4('3')
//    test5()
//    test6()
//    test7()
//    test8()
//    test9()
//    test10()
//    test11()
//    test12()
//    test13()
//    test14()
//    test15()
//    test16()
//    test17()
}

/**
 * 构造器
 */
private fun test17() {
    //TEST1只有两个构造器可用,另一个私有不可用
//    Test1("HELLO")
//    Test1("HELLO",12)
    //只有一个构造器,没有无参构造器
//    Test2(1)
    //TEST3只有两个构造器可用
//    Test3()
//    Test3("111")
//调用无参构造器时,会先执行初始化,后执行最后一个都带有默认值的构造器
//    Test4()
//    Test4("111")
//    Test4("222",10)
//    Test4("333",10,"上海")
}

/**
 * 方法的重写
 */
private fun test16() {
    val ccc = CCC()
    ccc.a()
    ccc.b()
    ccc.f()
    ccc.c()
    ccc.c("123")
    ccc.d(1)
    ccc.d("456")
    ccc.e()
}

/**
 * 带有便签的返回值
 */
private fun test14() {
    //使用标签控制 return 的目标
    foo1()
    foo2()
    foo3()
}

/**
 * 构造器的相关
 */
private fun test15() {
    Dog(4)
    Cat(2)
    Man("Tom", 10)
    SmallWoman("Jarry", 10, "北京", "中国")
}

private fun foo1() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return // 非局部的返回(non-local return), 直接返回到 foo() 函数的调用者
        println(it)
    }
    println("this point is unreachable")
}

private fun foo2() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach // 局部的返回(local return), 返回到 Lambda 表达式的调用者, 也就是, 返回到 forEach 循环
        println(it)
    }
    println(" done with explicit label")
}

private fun foo3() {
    run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop // 非局部的返回(non-local return), 从传递给 run 函数的 Lambda 表达式中返回
            print(it)
        }
    }
    print(" done with nested loop")
}

/**
 * 标签循环中的使用
 */
private fun test13() {
    for (i in 1..20) {
        for (j in 1..20) {
            if (j == 10) {
                break
            }
            println(j)
        }
    }
    println("---------------")
    loop@ for (i in 1..20) {
        for (j in 1..20) {
            if (j == 10) {
                break@loop
            }
            println(j)
        }
    }
    println("---------------------------")
    ab@ for (i in 1..20) {
        for (j in 1..20) {
            if (j == 10) {
                continue@ab
            }
            println(j)
        }
    }
}

/**
 * while循环,break,continue
 */
private fun test12() {
    var x = 10
    while (x > 1) {
        x--
        println(x)
    }

    var y = 10
    do {
        y++
        println(y)
    } while (y < 20)

    var z: Int = 10
    while (true) {
        z++
        if (z == 15) {
            continue
        }
        if (z == 20) {
            break
        }
        println(z)
    }
}

/**
 * for循环
 */
private fun test11() {
    for (i in 1..10) {
        println(i)
    }

    for (i in 20 downTo 10 step 2) {
        println(i)
    }

    val array = arrayOf("1", "2", "3")
    for (i in array.indices) {
        println(array[i])
    }

    for ((index, value) in array.withIndex()) {
        println("this e at $index is $value")
    }
}

/**
 * when语句
 */
private fun test10() {
    val x = 1
    when (x) {
        1 -> println("x==1")
        2 -> println("x==2")
        else -> {
            println("x")
        }
    }
    val str: String = when (x) {
        1, 2 -> "1,2"
        else -> "other"
    }
    println(str)
    when (x) {
        parseInt("2") -> println("22")
        else -> println("other")
    }
    when (x) {
        in 1..10 -> {
            println("1..10")
        }
        !in 10..20 -> {
            println("10..20")
        }
        else -> {
            println("....")
        }
    }

    fun hasTest(y: Any) = when (y) {
        is String -> {
            println(y.startsWith("123"))
        }
        else -> {
            println("other")
        }
    }
    hasTest("qwe")

    val string: String = "qwe"
    when {
        string.equals("123") -> {
            println("123")
        }
        string.startsWith("qwe") -> {
            println("qwe")
        }
        else -> {
            println("other")
        }
    }

    /*
    , 将 when 语句的判断对象保存到一个变量中:
    由 when 引入的这个变量, 它的有效范围仅限于 when 语句之内.
    fun Request.getBody() =
            when (val response = executeRequest()) {
                is Success -> response.body
                is HttpError -> throw HttpException(response.status)
            }*/
}

/**
 * if语句
 */
private fun test9() {
    val a: Int = 10
    val b: Int = 20
    val value = if (a > b) {
        println(a)
        a
    } else {
        println(b)
        b
    }
    println(value)
}

/**
 * 字符串模版
 */
private fun test8() {
    val i: Int = 10
    println("i=$i")

    val str: String = "abc"
    println("$str.length is ${str.length}")
    val price = """
${'$'}9.99
"""
    println(price)
}

/**
 * 字符串两种字面值
 * 转义字符串: "丰东股份的"
 * 原生字符串:"""等丰富的非"""
 * */
private fun test7() {
    val text1 = """
    for (c in "foo")
        print(c)
"""
    println(text1)
    /*可以使用 trimMargin() 函数来删除字符串的前导空白,
    默认情况下, 会使用 | 作为前导空白的标记前缀, 但你可以通过参数指定使用其它字符, 比如 trimMargin(">").*/
    val text2 = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()
    println(text2)
}

/**
 * 字符串
 */
private fun test6() {
    val str: String = "abc"
    for (c in str) {
        println(c)
    }
    val newStr: String = "def" + 1
    println(newStr + "qwe")
}

/**
 * 创建数组*/
private fun test5() {
    val arrA = arrayOf(1, 2, 3)
    val arrB = arrayOfNulls<Int>(3)

    val num: (Int) -> Int = { i -> i * i }
    val arrC = Array(3, num)

    val intArray = intArrayOf(1, 2, 3)
}

/**
 * 字符类型
 */
fun test4(c: Char) {
    if (c !in '0'..'9') {
        throw IllegalArgumentException("Out of range")
    }
    println(c.toFloat() - '0'.toInt()) // 显式转换为数值
}

/**
 * 运算符 */
private fun test3() {
    println(1.0 == 1.0)
    println(1 != 1)
    println(1 in 0..10)
}

/**
 * 数值的显示类型转换 */
private fun test2() {
    val b: Byte = 1
    val i: Int = b.toInt()
    print(i)
}

/**
 * ===比较地址值
 * ==比较内容
 * */
private fun test1() {
    //sampleStart
    val a: Int = 10000
    println(a === a) // 打印结果为 'true'
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA === anotherBoxedA) // !!!打印结果为 'false'!!!
//sampleEnd
    //sampleStart
    val b: Int = 10000
    println(b == b) // 打印结果为 'true'
    val boxedB: Int? = b
    val anotherBoxedB: Int? = b
    println(boxedB == anotherBoxedB) // 打印结果为 'true'
//sampleEnd
}