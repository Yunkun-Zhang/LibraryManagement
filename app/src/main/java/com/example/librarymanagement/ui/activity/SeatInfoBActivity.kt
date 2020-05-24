package com.example.librarymanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.R
import com.example.librarymanagement.ui.activity.SeatInfoCActivity
import kotlinx.android.synthetic.main.activity_seat_info_b.*

class SeatInfoBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_info_b)
        btn_a.setOnClickListener {
            val intent = Intent(this, SeatInfoActivity::class.java)
            startActivity(intent)
        }
        btn_c.setOnClickListener {
            val intent = Intent(this, SeatInfoCActivity::class.java)
            startActivity(intent)
        }
    }
}
