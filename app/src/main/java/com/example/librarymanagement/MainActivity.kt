package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.ui.activity.Book
import com.example.librarymanagement.ui.activity.Login
import com.example.librarymanagement.ui.activity.Signup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置

        // 获取userID
        var userID = intent.getIntExtra("userID", 0)
        var seat = intent.getIntExtra("seat", -1)
        if (seat != -1) {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("至少预订1个小时！")
            alertDialog.setNeutralButton("确定", null)
            alertDialog.show()
        }

        // 界面的一些显示更改
        if (userID != 0) {
            login_state.text = "空闲"
            log_or_sign.visibility = View.GONE
            logout.visibility = View.VISIBLE
        }
        else {
            login_state.text = "未登录"
            log_or_sign.visibility = View.VISIBLE
            logout.visibility = View.GONE
        }

        main_to_login.setOnClickListener {
            Intent(this, Login::class.java).apply {
                startActivity(this)
            }
        }

        main_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply {
                startActivity(this)
            }
        }

        main_to_book.setOnClickListener {
            // 先判断是否登录
            if (login_state.text.toString() == "未登录") {
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else {
                Intent(this, Book::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }

        logout.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }


    }

}

