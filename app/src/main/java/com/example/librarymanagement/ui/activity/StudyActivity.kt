package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_study.*

class StudyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val userID = intent.getIntExtra("userID", 0)

        var countDownTimer: CountDownTimer
        countDownTimer = object : CountDownTimer(10000000, 1000) {
            override fun onTick(secondsUntilDone: Long) {
                var h = hour.text.toString().toInt()
                var m = min.text.toString().toInt()
                var s = second.text.toString().toInt()
                s = s + 1
                if (s >= 60) {
                    s = 0
                    m = m + 1
                }
                if (m >= 60) {
                    m = 0
                    h = h + 1
                }
                if (h >= 10) hour.text = "$h"
                else hour.text = "0$h"
                if (m >= 10) min.text = "$m"
                else min.text = "0$m"
                if (s >= 10) second.text = "$s"
                else second.text = "0$s"
            }
            override fun onFinish() { }
        }.start()

        leave.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                putExtra("userID", userID)
                val h = hour.text.toString()
                val m = min.text.toString()
                val s = min.text.toString()
                putExtra("hour", h)
                putExtra("min", m)
                putExtra("second", s)
                startActivity(this)
            }
        }
    }
}