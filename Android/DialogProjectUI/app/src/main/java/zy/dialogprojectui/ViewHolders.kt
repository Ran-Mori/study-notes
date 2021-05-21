package zy.dialogprojectui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//密封类的一大好处是在做子类判断时不用写那个多余的else
//此处先设置一个父类，因为消息的类型有两种，因此对应的ViewHolder也有两种、
sealed class MessageViewHolder(view: View): RecyclerView.ViewHolder(view)

//左右ViewHolder的作用就是存储一个TextView
class LeftMessageViewHolder(view: View):MessageViewHolder(view){
    val message = view.findViewById<TextView>(R.id.leftMsg)
}
class RightMessageViewHolder(view: View):MessageViewHolder(view){
    val message = view.findViewById<TextView>(R.id.rightMsg)
}