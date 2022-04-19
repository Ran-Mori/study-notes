package builder

fun main(args:Array<String>){
    val studentA = Student.getBuilder()
        .setAge(20)
        .setName("Tom")
        .setSex(true)
        .build()

    println(studentA)

    val studentB = Student.getBuilder()
        .setAge(30)
        .setName("Alice")
        .setSex(false)
        .build()

    println(studentB)
}