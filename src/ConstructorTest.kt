/**
 * Kotlin 中所有的类都有一个共同的超类 Any, 如果类声明时没有指定超类, 则默认为 Any
 * Any 不是 java.lang.Object; 尤其要注意, 除 equals(), hashCode() 和 toString() 之外, 它没有任何成员
 */
open class Person(age: Int) {
    init {
        println("Person init")
    }
}

/**
 * 如果子类有主构造器, 必须在主构造器中使用主构造器的参数来初始化基类.
 */
class Man(name: String, age: Int) : Person(age) {
    init {
        println("Man init")
    }
}

/**
 * 如果类没有主构造器, 那么所有的次级构造器都必须使用 super 关键字来初始化基类, 或者委托到另一个构造器, 由被委托的构造器来初始化基类.
 * 注意, 这种情况下, 不同的次级构造器可以调用基类中不同的构造器:
 */
open class Woman : Person {
    init {
        println("Woman init")
    }

    constructor(name: String, age: Int) : super(age)

    constructor(name: String, age: Int, height: Int, weight: Int) : super(age)
}

class SmallWoman : Woman {
    init {
        println("SmallWoman init")
    }

    constructor(name: String, age: Int, school: String) : super(name, age)
    constructor(name: String, age: Int, height: Int, weight: Int, local: String) : super(name, age, height, weight)
    constructor(name: String, age: Int, school: String, country: String) : this(name, age, school)
}

open class Animal {
    init {
        println("Animal init")
    }
}

class Dog : Animal {
    init {
        println("Dog init")
    }

    constructor(legNum: Int)
}

class Cat(age: Int) : Animal() {
    init {
        println("Cat init")
    }
}
