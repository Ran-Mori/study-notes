package zy.dialogprojectui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val messages:ArrayList<Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        initMessage()

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MsgAdapter(messages)
        recyclerView.adapter = adapter

        button.setOnClickListener {
            val text = editText.text.toString()
            if (text.length != 0){
                messages.add(Message(text, Message.SEND))
                //通知最后一个是新添加的
                adapter.notifyItemInserted(messages.size - 1)
                //把list滑动到底
                recyclerView.scrollToPosition(messages.size - 1)
                //把输入框清空
                editText.setText("")
            }
        }

    }

    fun initMessage(){
        messages.apply {
            add(Message("A", Message.SEND))
            add(Message("B", Message.RECEIVE))
            add(Message("C", Message.SEND))
            add(Message("D", Message.RECEIVE))
            add(Message("E", Message.SEND))
            add(Message("F", Message.RECEIVE))
        }

    }
}