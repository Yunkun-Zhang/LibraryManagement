package com.example.librarymanagement.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.OrderDao
import com.example.librarymanagement.adapter.UserDao
import com.example.librarymanagement.control.OrderControl
import com.example.librarymanagement.database.AppDataBase
import kotlinx.android.synthetic.main.activity_book.*

import splitties.resources.int


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // 进入该页面前一定处于登录状态
        var userID = intent.getIntExtra("userID", -1)
        var seatID = intent.getIntExtra("seatID", 0)
        var start = intent.getIntExtra("start", 8)
        var end = intent.getIntExtra("end", 23)

        start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
        end_time.text = Editable.Factory.getInstance().newEditable(end.toString())

        // 取消
        book_back.setOnClickListener {
            finish()
        }

        // 确定
        var orderControl = OrderControl()

        confirm.setOnClickListener {
            start = start_time.text.toString().toInt()
            end = end_time.text.toString().toInt()
            var sub = subject.text.toString()
            var g : Boolean?

            if (gender.selectedItem == "男") g = true
            else if (gender.selectedItem == "女") g = false
            else g = null
            var valid_seats = orderControl.findPairedSeats(sub, g, start, end)
            var order = orderControl.create_order(userID, seatID, start, end, false, sub, g)
            orderControl.confirmOrder(order)

            Intent(this, MainActivity::class.java).apply {
                putExtra("seat", seatID)
                putExtra("userID", userID)
                startActivity(this)
            }
        }

        // 选座
        choose_seat.setOnClickListener {
            start = start_time.text.toString().toInt()
            end = end_time.text.toString().toInt()
            // 8:00 -- 23:00
            if (start < 8 || start > 23 || end < 8 || end > 23) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("请输入正确的时间！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
            else if (start >= end) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("至少预订1个小时！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
            else {
                Intent(this, SeatInfoActivity::class.java).apply {
                    putExtra("start", start)
                    putExtra("end", end)
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }

    }


}