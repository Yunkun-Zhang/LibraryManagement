package com.example.librarymanagement.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_seat_info.*
import com.example.librarymanagement.control.*
import splitties.bundle.putExtras

class SeatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_info)
        var app = MyApplication.instance()
        var seatControl = app.seatControl
        // var orderControl  =app.orderControl

        val userID = intent.getIntExtra("userID", 0)


        var start = intent.getIntExtra("start", 8)
        var end = intent.getIntExtra("end", 23)

        var seat_list = seatControl.querySeatByTime(start, end)

        var seatID = 0

        s1081.setOnClickListener {
            if (seatID != 1081) {
                ss1081.visibility = View.VISIBLE
                seatID = 1081
            }
        }
        ss1081.setOnClickListener {
            ss1081.visibility = View.GONE
            seatID = 0
        }

        btn_b.setOnClickListener {
            val intent = Intent(this, SeatInfoBActivity::class.java)
            startActivity(intent)
        }
        btn_c.setOnClickListener {
            val intent = Intent(this, SeatInfoCActivity::class.java)
            startActivity(intent)
        }
        confirm.setOnClickListener {
            val data = Intent(this, Book::class.java)

            data.putExtra("start", start)
            data.putExtra("end", end)
            data.putExtra("seatID", seatID)
            data.putExtra("userID", userID)

            startActivity(data)
        }
        btn_back.setOnClickListener {
            finish()
        }
    }
}
