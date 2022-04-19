package builder

class StudentBuilder {
    private val student = Student()
    fun setName(name:String):StudentBuilder{
        student.name = name
        return this
    }
    fun setAge(age:Int):StudentBuilder{
        student.age = age
        return this
    }
    fun setSex(sex:Boolean):StudentBuilder{
        student.sex = sex
        return this
    }
    fun build() = student
}