package zy.dialogprojectui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//此处的ViewHolder是父类MessageViewHolder
class MsgAdapter(val msgs:List<Message>):RecyclerView.Adapter<MessageViewHolder>() {

    //这是一个之前没有用到的方法，list的每一个项的类型可以是不同的，用这个函数可以获取类型
    override fun getItemViewType(position: Int): Int {
        Log.d("MainActivity","getItemViewType")
        return msgs[position].type
    }

    //此处利用到了第二个参数viewType：Int，之前都是没有用到过的
    //且此处返回的是父类MessageViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        Log.d("MainActivity","onCreateViewHolder")
        if (viewType == Message.SEND){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right,parent,false)
            return RightMessageViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left,parent,false)
            return LeftMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        Log.d("MainActivity","getItemCount()")
        return msgs.size
    }

    //此处要进行一个类型判断，这种时候密封类的好处就体现出来了
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        Log.d("MainActivity","onBindViewHolder")
        val msg = msgs[position].msg
        when(holder){
            is LeftMessageViewHolder -> holder.message.text = msg
            is RightMessageViewHolder -> holder.message.text = msg
        }
    }
}