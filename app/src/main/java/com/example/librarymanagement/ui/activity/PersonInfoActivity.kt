package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.librarymanagement.R
import com.example.librarymanagement.extension.StartConversation
import com.example.librarymanagement.model.Reservation
import com.example.librarymanagement.model.User
import com.example.librarymanagement.others.UserStatus
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_person_info.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class PersonInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        val userID = intent.getIntExtra("userID", 0)
        val now_seat = intent.getIntExtra("now_seat", 0)
        // 下两个变量表示从订单界面来，可加好友
        val friend = intent.getBooleanExtra("friend", true)
        val friendid = intent.getIntExtra("friendid", 0)

        if (userID != 0 && friendid == 0) { // 自己
            send_text.visibility = View.GONE
            // 获取userID对应用户的信息，修改显示
            Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userid" to userID.toString()))
                .post(object : CallbackRule<User> {
                    override suspend fun onFailed(error: String) {
                        alert("请检查网络") {
                            setTitle("个人信息获取失败")
                            positiveButton("确定") {}
                        }.show()
                    }

                    override suspend fun onSuccess(entity: User, flag: String) {
                        //显示这些信息
                        name.text = entity.name
                        if (entity.gender == true) gender.text = "男"
                        else if (entity.gender == false) gender.text = "女"
                        else gender.text = ""
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
            to_order.setOnClickListener {
                Intent(this@PersonInfoActivity, OrderActivity::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }
        else {  // 好友
            mod.text = ""
            val friendname = intent.getStringExtra("friendname")
            // 显示好友（用户）信息
            if (friend) { // 如果已经是好友
                Okkt.instance.Builder().setUrl("/user/findbyname").setParams(hashMapOf("name" to friendname))
                    .get(object : CallbackRule<User> {
                        override suspend fun onFailed(error: String) {
                            alert("请检查网络") {
                                title = "个人信息获取失败"
                                positiveButton("确定") {}
                            }.show()
                        }
                        override suspend fun onSuccess(entity: User, flag: String) {
                            //显示这些信息
                            name.text = entity.name
                            if (entity.gender == true) gender.text = "男"
                            else if (entity.gender == false) gender.text = "女"
                            else gender.text = ""
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
                                    StartConversation().startConversation(
                                        entity.userID.toString(),
                                        this@PersonInfoActivity
                                    )
                                }
                            }
                        }
                    })
            }
            else { // 从订单记录跳转来，非好友，可加为好友
                send_text.text = "加为好友"
                Okkt.instance.Builder().setUrl("/user/findbyuserid")
                    .setParams(hashMapOf("userid" to friendid.toString()))
                    .get(object : CallbackRule<User> {
                        override suspend fun onFailed(error: String) {
                            alert("请检查网络") {
                                setTitle("个人信息获取失败")
                                positiveButton("确定") {}
                            }.show()
                        }
                        override suspend fun onSuccess(entity: User, flag: String) {
                            //显示这些信息
                            name.text = entity.name
                            if (entity.gender == true) gender.text = "男"
                            else if (entity.gender == false) gender.text = "女"
                            else gender.text = ""
                            phone.text = entity.phone
                            email.text = entity.email
                            favorsubject.text = entity.favorsubject
                            if (entity.status == UserStatus.FREE) status.text = "空闲"
                            else if (entity.status == UserStatus.ACTIVE) status.text = "占座中"
                            else status.text = "暂离"
                            // 发送好友申请
                            send_text.setOnClickListener {
                                // 获取自己信息
                                Okkt.instance.Builder().setUrl("/user/findbyuserid")
                                    .putBody(hashMapOf("userid" to userID.toString()))
                                    .post(object : CallbackRule<User> {
                                        override suspend fun onFailed(error: String) {
                                            toast("没找到自己")
                                        }
                                        override suspend fun onSuccess(user: User, flag: String) {
                                            // 获取好友
                                            Okkt.instance.Builder().setUrl("/invite/add")
                                                .setParams(
                                                    hashMapOf(
                                                        "sendID" to userID.toString(),
                                                        "sendname" to user.name,
                                                        "receiveID" to friendid.toString()
                                                    )
                                                )
                                                .post(object : CallbackRule<String> {
                                                    override suspend fun onFailed(error: String) {
                                                        alert("成功发送好友申请！") {}.show()
                                                    }
                                                    override suspend fun onSuccess(
                                                        entity: String,
                                                        flag: String
                                                    ) {
                                                        alert("成功发送好友申请！") {}.show()
                                                    }
                                                })
                                        }
                                    })
                            }
                        }
                    })
            }
        }

        // 返回
        btn_back.setOnClickListener { finish() }
    }
}
