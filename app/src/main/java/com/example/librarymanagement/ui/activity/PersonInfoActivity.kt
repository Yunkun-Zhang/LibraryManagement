package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.librarymanagement.R
import com.example.librarymanagement.extension.StartConversation
import kotlinx.android.synthetic.main.activity_person_info.*

class PersonInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        val userID = intent.getIntExtra("userID", 0)
        val friend = intent.getBooleanExtra("friend", false)

        // 获取userID对应用户的信息，修改显示


        if (!friend) {
            send_text.visibility = View.GONE
            team_book.visibility = View.GONE
        }
        else {
            //创建新对话
            send_text.setOnClickListener {
                Intent(this, ConversationActivity::class.java).apply {
                    //开始和谁对话就修改那个useid，就可以打开对应的对话界面
                    StartConversation().startConversation(userID.toString(), this@PersonInfoActivity)
                }
            }
        }

        btn_back.setOnClickListener { finish() }
    }
}
