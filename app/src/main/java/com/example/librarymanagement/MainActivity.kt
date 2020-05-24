package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.extension.DateUtil
// import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.ui.activity.Book
import com.example.librarymanagement.ui.activity.Login
import com.example.librarymanagement.ui.activity.Signup
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.jvm.internal.impl.types.AbstractTypeCheckerContext


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置

        // 获取userID
        var userID  = 0
        userID = intent.extras.getInt("userID")

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
        var app = MyApplication.instance()
        var seatControl = app.seatControl

        /*
        index.setOnClickListener {
            //val intent = Intent(this, RoomTest::class.java)
            //startActivity(intent)
            Intent(this, RoomTest::class.java).apply {
                // 传递参数
                putExtra("bar", "Peter")
                putExtra("info", 33)

                //传递一个整体(类）参数
                // putExtra("user", User())
                // 设置跳转

                startActivity(this)
                // startActivityForResult()    可传回数据
            }
        }

         */
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

        val oDao = AppDataBase.instance.getOrderDao()
        var x = oDao.getOrderByGender(male = true)
        var y = oDao.getOrderByTimePeriod(6,9)

    }
}

