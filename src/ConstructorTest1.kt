/**
 * 字段,构造器初始化谁写前面,谁先执行,被调用的构造器实体最后执行
 * 对构造器私有化方式
 */
class Test1(name: String) {
    //class Test private constructor(name: String) {//构造方法前有修饰符或注解必须要写constructor
    //class Test private constructor() {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }

    constructor(name: String, age: Int) : this(name) {
        println(age)
    }

    private constructor(name: String, age: Int, school: String) : this(name) {
        println(school)
    }
}

/**
 * 不写主构造函数,次级构造函数会隐式调用无参主构造函数,并调用初始化代码
 */
class Test2 {
    init {
        println("Init block")
    }

    constructor(i: Int) {
        println("Test2")
    }
}

/**
 * 构造函数的参数都有默认值时,可以使用无参构造函数
 */
class Test3(val name: String = "0000") {
    init {
        println(name)
    }
}

/**
 *  没有主构造函数,只有次级构造函数,其次级中的参数无法在初始化代码中使用
 *  调用无参构造器时,会先执行初始化,后执行最后一个都带有默认值的构造器
 */
class Test4 {
    init {
        println("Test4:init")
    }

    constructor(name: String = "++++", age: Int = 1, school: String = "北京") {
        println(name)
    }

    constructor(name: String = "----", age: Int = 2) {
        println(name)
    }

    constructor(name: String) {
        println(name)
    }
}