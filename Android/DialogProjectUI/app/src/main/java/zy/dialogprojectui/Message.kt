package zy.dialogprojectui

class Message(val msg:String?,val type:Int) {
    companion object{
        const val SEND:Int = 1
        const val RECEIVE:Int = 0
    }
}