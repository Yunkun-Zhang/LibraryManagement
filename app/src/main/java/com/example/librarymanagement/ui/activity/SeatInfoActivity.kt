package com.example.librarymanagement.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_seat_info.*
import com.example.librarymanagement.control.*
import org.jetbrains.anko.backgroundDrawable
import splitties.bundle.putExtras

class SeatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_info)
        var app = MyApplication.instance()
        var seatControl = app.seatControl

        var start = intent.extras.getInt("start")
        var end = intent.extras.getInt("end")

        var seat_list = seatControl.querySeatByTime(start, end)

        var seatID = 1081

        s1081.setOnClickListener {
            if (seatID != 1081) s1081.setBackgroundColor(R.drawable.shape_chosen)
            else s1081.setBackgroundColor(R.drawable.shape_green)
        }

        btn_b.setOnClickListener {
            val intent = Intent(this, SeatInfoBActivity::class.java)
            startActivity(intent)
        }
        btn_c.setOnClickListener {
            val intent = Intent(this, SeatInfoCActivity::class.java)
            startActivity(intent)
        }
        btn_back.setOnClickListener {
            val data = Intent()

            data.putExtra("seatID", seatID)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
