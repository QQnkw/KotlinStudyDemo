class E {
    //        var a: Int?// 错误: 需要明确指定初始化器, 此处会隐含地使用默认的取值方法和设值方法
    var b: Int = 1// 属性类型为 Int, 使用默认的取值方法和设值方法

    //    val c: Int? // 属性类型为 Int, 使用默认的取值方法, 属性值必须在构造器中初始化
    val d: Int = 1// 属性类型为 Int, 使用默认的取值方法

    /**
     * 可以为属性定义自定义的访问方法.
     * 如果定义一个自定义取值方法(Getter),
     * 那么每次读取属性值时都会调用这个方法
     * (因此我们可以用这种方式实现一个计算得到的属性).
     */
    val e: Int
        get() = 3 - 2

    /**
     * 如果我们定义一个自定义设值方法(Setter), 那么每次向属性赋值时都会调用这个方法.
     */
    var f: Int
        get() = 7 - 1
        set(value) {
            value - 10
        }

    /**
     * 如果你需要改变属性访问方法的可见度, 或者需要对其添加注解,
     * 但又不需要修改它的默认实现, 你可以定义这个方法, 但不定义它的实现体:
     */
    var g: Int = 1
        private set // 设值方法的可见度为 private, 并使用默认实现
//    @Inject get() = 2 // 对设值方法添加 Inject 注解

    /**
     * 属性的后端域变量
    Kotlin 的类不能直接声明域变量.
    但是, 如果属性需要一个后端域变量(Backing Field), Kotlin 会自动提供.
    在属性的取值方法或设值方法中, 使用 field 标识符可以引用这个后端域变量:
    field 标识符只允许在属性的访问器函数内使用.
     */
    var h: Int = 10
        set(value) {
            if (value > 0) {
                field += value
            }
        }

    /**
     * 后端属性(Backing Property)
    如果你希望实现的功能无法通过这种 “隐含的后端域变量” 方案来解决,
    你可以使用 后端属性(backing property) 作为替代方案:
     */
    private var i: Int? = null
    public val ii: Int
        get() {
            if (i == null) {
                i = 10 * 10
            }
            return i ?: -10
        }

    /**
     * 编译期常数值
     * 如果属性值在编译期间就能确定,
     * 则可以使用 const 修饰符, 将属性标记为编译期常数值(compile time constants).
     * 这类属性必须满足以下所有条件:
     * 1.必须是顶级属性, 或者是一个 object 声明 的成员, 或者是一个 companion object 的成员.
     * 2.值被初始化为 String 类型, 或基本类型(primitive type)
     * 3.不存在自定义的取值方法
    这类属性可以用在注解内:
     */
    /*const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"
    @Deprecated(SUBSYSTEM_DEPRECATED) fun foo() { ... }*/

    /**
     * 延迟初始化变量关键字lateinit
     * 这个修饰符可以用于类主体部分之内声明的 var 属性,
     * (不是主构造器中声明的属性, 而且属性没有自定义的取值方法和设值方法).
     * 从 Kotlin 1.2 开始, 这个修饰符也可以用于顶级(top-level)属性和局部变量.
     * 属性或变量的类型必须是非 null 的, 而且不能是基本类型.
     *
     * 为了检查一个 lateinit var 是否已经初始化完成,
     * 可以对 属性的引用 调用 .isInitialized:
     * if (e::a.isInitialized) {
     * println(e.a)
     * }
     * 这种检查只能用于当前代码可以访问到的属性,
     * 也就是说, 属性必须定义在当前代码的同一个类中,
     * 或当前代码的外部类中, 或者是同一个源代码文件中的顶级属性.
     */
    lateinit var a: A
    fun initA() {
        a = A()
    }
}