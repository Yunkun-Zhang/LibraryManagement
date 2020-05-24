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
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.UserDao
import com.example.librarymanagement.database.AppDataBase
import kotlinx.android.synthetic.main.activity_book.*
import org.jetbrains.anko.sdk25.coroutines.onTimeChanged
import org.jetbrains.anko.sdk25.coroutines.textChangedListener
import splitties.resources.int


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // 进入该页面前一定处于登录状态
        var userID = intent.extras.getInt("userID")

        val uDao: UserDao = AppDataBase.instance.getUserDao()
        var user = uDao.getUserID(userID)

        book_back.setOnClickListener {
            finish()
        }

        choose_seat.setOnClickListener {
            var start = start_time.text.toString().toInt()
            var end = end_time.text.toString().toInt()
            // 7:00 -- 23:00
            if (start < 7 || start > 23 || end < 7 || end > 23) {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("请输入正确的时间！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
            else if (start >= end) {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("至少预订1个小时！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
            else {
                Intent(this, SeatInfoActivity::class.java).apply {
                    putExtra("start", start)
                    putExtra("end", end)
                    startActivity(this)
                }
            }
        }

    }

}