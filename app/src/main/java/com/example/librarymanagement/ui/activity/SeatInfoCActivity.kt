package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.R
import com.example.librarymanagement.ui.activity.SeatInfoActivity
import com.example.librarymanagement.ui.activity.SeatInfoBActivity
import kotlinx.android.synthetic.main.activity_seat_info_c.*

class SeatInfoCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_info_c)
        btn_a.setOnClickListener {
            val intent = Intent(this, SeatInfoActivity::class.java)
            startActivity(intent)
        }
        btn_b.setOnClickListener {
            val intent = Intent(this, SeatInfoBActivity::class.java)
            startActivity(intent)
        }
    }
}
