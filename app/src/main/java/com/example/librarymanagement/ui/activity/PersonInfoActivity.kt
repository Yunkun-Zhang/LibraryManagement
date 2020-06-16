package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.librarymanagement.R
import com.example.librarymanagement.extension.StartConversation
import com.example.librarymanagement.model.User
import com.example.librarymanagement.others.UserStatus
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_person_info.*

class PersonInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        val userID = intent.getIntExtra("userID", 0)
        val friend = intent.getBooleanExtra("friend", false)

        // 获取userID对应用户的信息，修改显示
        Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userId" to userID.toString())).
        post(object: CallbackRule<User> {
            override suspend fun onFailed(error: String) {
                val alertDialog = AlertDialog.Builder(this@PersonInfoActivity)
                alertDialog.setTitle("个人信息获取失败")
                alertDialog.setMessage("请检查网络")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }

            override suspend fun onSuccess(entity: User, flag: String) {
                //显示这些信息
                name.text = entity.name
                if (entity.gender == true) gender.text = "男"
                else gender.text = "女"
                phone.text = entity.phone
                favorsubject.text = entity.favorsubject
                if (entity.status == UserStatus.FREE) status.text = "空闲"
                else if (entity.status == UserStatus.ACTIVE) status.text = "占座中"
                else status.text = "暂离"
            }
        })

        if (!friend) {
            send_text.visibility = View.GONE
            team_book.visibility = View.GONE
            mod.text = ""
        }
        else {
            //创建新对话
            send_text.setOnClickListener {
                Intent(this, ConversationActivity::class.java).apply {
                    //开始和谁对话就修改那个useid，就可以打开对应的对话界面
                    StartConversation().startConversation(userID.toString(), this@PersonInfoActivity)
                }
            }

            mod.setOnClickListener {
                Intent(this, ModInfoActivity::class.java).apply {

                }
            }

        }

        btn_back.setOnClickListener { finish() }
    }
}
