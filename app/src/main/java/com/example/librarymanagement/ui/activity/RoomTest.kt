package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.extension.DateUtil
import com.example.librarymanagement.model.UserTest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_test.*
import org.jetbrains.anko.dip

class RoomTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_test)
        //接收参数
        //val name = intent.extras?.getString("bar")
        //test_bar.text = name
        //test_info.text = intent.getIntExtra("info",0).toString()

        //按类接收参数

        var user = intent.getParcelableExtra("user") as UserTest //as User 确保是个User类型
        test_bar.text = user.bar
        test_info.text = user.info.toString() // 转换为string
        test_bar.setOnClickListener {
            //val intent = Intent(this, RoomTest::class.java)
            //startActivity(intent)
            Intent(this, MainActivity::class.java).apply { startActivity(this) }
        }

    }
}
