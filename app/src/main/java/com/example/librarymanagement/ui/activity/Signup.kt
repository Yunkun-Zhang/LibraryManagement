package com.example.librarymanagement.ui.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_signup.*


class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        book_back.setOnClickListener {
            finish()
        }

        signup_submit.setOnClickListener {
            if (username.text.toString() != "" && password.text.toString() != "") {
                if (password.text.toString() != confirm_password.text.toString()) {
                    var alertDialog = AlertDialog.Builder(this)
                    alertDialog.setMessage("确认密码与密码不符！")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }
                else {
                    var sign = Intent(this, Login::class.java)    // 注册完进入登录页面
                    // 此处应记录注册的用户
                    sign.putExtra("username", username.text.toString())
                    sign.putExtra("password", password.text.toString())
                    startActivity(sign)
                }
            }
            else {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("用户名或密码不应为空！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
        }
    }


}