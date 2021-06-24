package forceproxy

class StudentProxy(val student:IStudent):IStudent {

    override fun learn() {
        student.learn()
    }

    override fun getProxy(): IStudent? {
        return this
    }
}