package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val userID = intent.getIntExtra("userID",0)

        if (userID != 0) { // 自己
            send_text.visibility = View.GONE
            team_book.visibility = View.GONE
            // 获取userID对应用户的信息，修改显示
            Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userId" to userID.toString()))
                .post(object : CallbackRule<User> {
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
                        email.text = entity.email
                        favorsubject.text = entity.favorsubject
                        if (entity.status == UserStatus.FREE) status.text = "空闲"
                        else if (entity.status == UserStatus.ACTIVE) status.text = "占座中"
                        else status.text = "暂离"
                    }
                })
            mod.setOnClickListener {
                Intent(this@PersonInfoActivity, ModInfoActivity::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }
        else {  // 好友
            val friendname = intent.getStringExtra("friendname")
            mod.text = ""
            Okkt.instance.Builder().setUrl("/user/findbyname").putBody(hashMapOf("name" to friendname))
                .post(object : CallbackRule<User> {
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
                        email.text = entity.email
                        favorsubject.text = entity.favorsubject
                        if (entity.status == UserStatus.FREE) status.text = "空闲"
                        else if (entity.status == UserStatus.ACTIVE) status.text = "占座中"
                        else status.text = "暂离"

                        //创建新对话
                        send_text.setOnClickListener {
                            Intent(this@PersonInfoActivity, ConversationActivity::class.java).apply {
                                //开始和谁对话就修改那个useid，就可以打开对应的对话界面
                                StartConversation().startConversation(entity.userID.toString(), this@PersonInfoActivity)
                            }
                        }

                    }
                })
        }

        btn_back.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 获取userID对应用户的信息，修改显示
        val userID = data?.getIntExtra("userID", 0)
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
    }
}
