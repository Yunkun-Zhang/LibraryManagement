package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.others.UserStatus
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.StringCallback
import kotlinx.android.synthetic.main.activity_study.*

class StudyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val userID = intent.getIntExtra("userID", 0)
        val seatID = intent.getIntExtra("now_seat", 0)
        var h = intent.getIntExtra("hour", hour.text.toString().toInt())
        var m = intent.getIntExtra("min", min.text.toString().toInt())
        var s = intent.getIntExtra("second", second.text.toString().toInt())

        // 设置计时功能
        var countDownTimer: CountDownTimer
        countDownTimer = object : CountDownTimer(10000000, 1000) {
            override fun onTick(secondsUntilDone: Long) {
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
            Okkt.instance.Builder().setUrl("/user/revisestatusbyid")
                .setParams(hashMapOf("userid" to userID.toString()))
                .putBody(hashMapOf("status" to UserStatus.LEAVE.toString()))
                .post(object: CallbackRule<String> {
                    override suspend fun onFailed(error: String) { }
                    override suspend fun onSuccess(entity: String, flag: String) { }
                })
            Okkt.instance.Builder().setUrl("/seat/setseat/leave")
                .setParams(hashMapOf("seatid" to seatID.toString()))
                .post(object: CallbackRule<String> {
                    override suspend fun onFailed(error: String) { }
                    override suspend fun onSuccess(entity: String, flag: String) { }
                })
            Intent(this, MainActivity::class.java).apply {
                putExtra("userID", userID)
                putExtra("now_seat", seatID)
                val h = hour.text.toString().toInt()
                val m = min.text.toString().toInt()
                val s = second.text.toString().toInt()
                putExtra("hour", h)
                putExtra("min", m)
                putExtra("second", s)
                startActivity(this)
            }
            finish()
        }

        finish.setOnClickListener {
            Okkt.instance.Builder().setUrl("/user/revisestatusbyid")
                .setParams(hashMapOf("userid" to userID.toString(), "status" to UserStatus.FREE.toString()))
                .get(object : StringCallback {
                    override suspend fun onFailed(error: String) {}
                    override suspend fun onSuccess(entity: String, flag: String) {}
                })
            Okkt.instance.Builder().setUrl("/seat/setseat/finish")
                .setParams(hashMapOf("seatid" to seatID.toString()))
                .post(object : CallbackRule<String> {
                    override suspend fun onFailed(error: String) {}
                    override suspend fun onSuccess(entity: String, flag: String) {}
                })
            // 可以做出去加好友的界面
            Intent(this@StudyActivity, MainActivity::class.java).apply {
                putExtra("userID", userID)
                putExtra("now_seat", 0) // 清除现在占座
                startActivity(this)
            }
            finish()
        }
    }
}