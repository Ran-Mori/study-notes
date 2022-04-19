package builder

class Student(){
    var name:String? = null
    var age:Int? = null
    var sex:Boolean? = null
    companion object{
        fun getBuilder() = StudentBuilder()
    }

    override fun toString(): String {
        return "name = $name,age = $age, sex = $sex"
    }
}