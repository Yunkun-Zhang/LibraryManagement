package com.example.librarymanagement.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.librarymanagement.R
import com.example.librarymanagement.model.Users
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_person_info.*

class PersonInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        val userId = intent.getIntExtra("userID",-1)
        if(userId == -1){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Error")
            alertDialog.setMessage("userID Error")
            alertDialog.setNeutralButton("确定", null)
            alertDialog.show()
        }else{
            Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userId" to userId.toString())).
            post(object: CallbackRule<Users> {
                override suspend fun onFailed(error: String) {
                    val alertDialog = AlertDialog.Builder(this@PersonInfoActivity)
                    alertDialog.setTitle("个人信息获取失败")
                    alertDialog.setMessage("请检查网络")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }

                override suspend fun onSuccess(entity: Users, flag: String) {
                    //测试使用,实现时可以删掉
                    val alertDialog = AlertDialog.Builder(this@PersonInfoActivity)
                    alertDialog.setTitle("个人信息获取成功")
                    alertDialog.setMessage("Congratulations!!!")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()

                    //当前用户信息只能获取这些，后续需要等待服务器修改
                    val name = entity.name
                    val email = entity.email
                    val gender = entity.gender
                    val phone = entity.phone
                    val favorsubject = entity.favorsubject
                }
            })
        }

        btn_back.setOnClickListener { finish() }
    }
}
