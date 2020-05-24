package com.example.librarymanagement.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        book_back.setOnClickListener {
            finish()
        }

        var user = intent.getStringExtra("username")
        var pswd = intent.getStringExtra("password")

        login_submit.setOnClickListener {
            if (username.text.toString() == "s" && password.text.toString() == "1") {
                var intent = Intent(this, MainActivity::class.java)
                // 搞一个userID
                intent.putExtra("userID", 1)
                startActivity(intent)
            }
            else {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("登陆失败")
                alertDialog.setMessage("用户名或密码错误！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
        }

        login_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply{
                startActivity(this)
            }
        }

    }


}