package forceproxy

fun main(args:Array<String>){
    val student:IStudent = Student()

    student.learn()
    student.getProxy()?.learn()
}