package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.model.Reservation
import com.example.librarymanagement.model.User
import com.example.librarymanagement.others.UserStatus
import com.google.gson.Gson
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onFocusChange
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // 进入该页面前一定处于登录状态
        val userID = intent.getIntExtra("userID", 0)
        val seatID = intent.getIntExtra("seatID", 0)
        val now_seat = intent.getIntExtra("now_seat", 0)
        val state = intent.getStringExtra("state")

        // 先查看是否有今天的订单
        if (state == "已预订") {
            // 获取订单信息，如start=13,end=18
            //start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
            //end_time.text = Editable.Factory.getInstance().newEditable(end.toString())
            start_time.isEnabled = false
            end_time.isEnabled = false
            choose_seat.isEnabled = false
            subject.isEnabled = false
            gender.isEnabled = false
            // cancel.isEnabled = true
            alert("明天您有一个订单还未完成，不可预订！") {
                negativeButton("取消该预订") {
                    // 取消订单
                    Okkt.instance.Builder().setUrl("/reservation/findbyuserid")
                        .setParams(hashMapOf("userid" to userID.toString()))
                        .get(object: CallbackRule<MutableList<Reservation>> {
                            override suspend fun onFailed(error: String) { }
                            override suspend fun onSuccess(entity: MutableList<Reservation>, flag: String) {
                                Okkt.instance.Builder().setUrl("/seatwithreservation/deletereservation")
                                    .setParams(
                                        hashMapOf(
                                            "userid" to userID.toString(),
                                            "reservationid" to entity[0].reservationid.toString()
                                        )
                                    )
                                    .post(object : CallbackRule<String> {
                                        override suspend fun onFailed(error: String) { }
                                        override suspend fun onSuccess(entity: String, flag: String) {
                                            Intent(this@Book, MainActivity::class.java).apply {
                                                putExtra("now_seat", now_seat)
                                                putExtra("userID", userID)
                                                startActivity(this)
                                                finish()
                                            }
                                        }
                                    })
                            }
                        })
                }
                positiveButton("返回") { finish() }
            }.show()
        }
        else {
            // 取消=返回
            cancel.setOnClickListener { finish() }
            // 下一步：选座
            confirm.setOnClickListener {
                var start = start_time.text.toString().toInt()
                var end = end_time.text.toString().toInt()
                // 8:00 -- 23:00
                if (start < 8 || start > 23 || end < 8 || end > 23) {
                    alert("图书馆在8点至23点开放") {
                        title = "时间错误"
                        positiveButton("确定") {}
                    }.show()
                }
                else if (start >= end) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setMessage("至少预订1个小时！")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }
                else {
                    val sub = subject.text.toString()
                    val g: Boolean?

                    if (gender.selectedItem == "男") g = true
                    else if (gender.selectedItem == "女") g = false
                    else g = null
                    // 没有匹配信息
                    if (sub == "" && g == null) {
                        Okkt.instance.Builder().setUrl("/seat/getspareseats/tomorrow")
                            .putBody(hashMapOf("starttime" to start.toString(), "endtime" to end.toString()))
                            .post(object : CallbackRule<MutableList<Int>> {
                                override suspend fun onFailed(error: String) {}
                                override suspend fun onSuccess(entity: MutableList<Int>, flag: String) {
                                    if (entity.isEmpty()) {
                                        alert("哎呀，没有可用的座位了！") {
                                            positiveButton("修改时间段") { }
                                        }.show()
                                    }
                                    else {
                                        Intent(this@Book, SeatInfoActivity::class.java).apply {
                                            putExtra("start", start)
                                            putExtra("end", end)
                                            putExtra("userID", userID)
                                            putExtra("now_seat", now_seat)
                                            putExtra("list", entity.toIntArray())
                                            startActivity(this)
                                        }
                                        finish()
                                    }
                                }
                            })
                    }
                    // 进行匹配
                    else {
                        // 先寻找自己的性别
                        Okkt.instance.Builder().setUrl("/user/findbyuserid")
                            .setParams(hashMapOf("userid" to userID.toString())).get(object : CallbackRule<User> {
                                override suspend fun onFailed(error: String) {
                                    toast("failed")
                                }
                                override suspend fun onSuccess(entity: User, flag: String) {
                                    val sg = entity.gender
                                    Log.w("my gender", sg.toString())
                                    var map: HashMap<String, String> = hashMapOf()
                                    map["starttime"] = start.toString()
                                    map["endtime"] = end.toString()
                                    if (sg != null) map["selfgender"] = sg.toString()
                                    if (g != null) map["targetgender"] = g.toString()
                                    if (sub != "") map["subject"] = sub.toString()
                                    //成功，先进行对匹配位置的第一次搜索
                                    Okkt.instance.Builder().setUrl("/seatwithreservation/findseatwithadjacentreservation")
                                        .setParams(map)
                                        .get(object : CallbackRule<HashMap<Int, List<Int>>> {
                                            override suspend fun onFailed(error: String) {
                                                // 如果为空究竟返回哪个地方？
                                                // alert("这说明最大的寻找尝试失败了") {positiveButton("修改时间段") { } }.show()
                                                Okkt.instance.Builder().setUrl("/seat/getspareseat/withspareadjacent")
                                                    .putBody(
                                                        hashMapOf(
                                                            "starttime" to start.toString(),
                                                            "endtime" to end.toString()
                                                        )
                                                    )
                                                    .post(object : CallbackRule<MutableList<Int>> {
                                                        override suspend fun onFailed(error: String) {}
                                                        override suspend fun onSuccess(
                                                            entity: MutableList<Int>,
                                                            flag: String
                                                        ) {
                                                            if (entity.isEmpty()) {  // 没有座位
                                                                alert("哎呀，没有可用的座位了！") {
                                                                    positiveButton("修改时间段") { }
                                                                }.show()
                                                            } else { // 有边上有空的座位
                                                                Intent(this@Book, SeatInfoActivity::class.java).apply {
                                                                    putExtra("start", start)
                                                                    putExtra("end", end)
                                                                    putExtra("userID", userID)
                                                                    putExtra("now_seat", now_seat)
                                                                    putExtra("subject", sub)
                                                                    if (g != null) putExtra("targetgender", g)
                                                                    if (sg != null) putExtra("selfgender", sg)
                                                                    putExtra("pair", true)
                                                                    putExtra("list", entity.toIntArray())
                                                                    putExtra("wait", true)
                                                                    startActivity(this)
                                                                }
                                                                finish()
                                                            }
                                                        }
                                                    })
                                                Log.w("bobbob", "failed") }
                                            override suspend fun onSuccess(entity: HashMap<Int, List<Int>>, flag: String) {
                                                // alert("这说明最大的寻找尝试成功了") {positiveButton("修改时间段") { } }.show()
                                                Log.w("bobbob", entity.keys.toIntArray()[0].toString())
                                                if (entity.isEmpty()) { // 希望匹配的无座位
                                                    Okkt.instance.Builder().setUrl("/seat/getspareseat/withspareadjacent")
                                                        .putBody(
                                                            hashMapOf(
                                                                "starttime" to start.toString(),
                                                                "endtime" to end.toString()
                                                            )
                                                        )
                                                        .post(object : CallbackRule<MutableList<Int>> {
                                                            override suspend fun onFailed(error: String) {}
                                                            override suspend fun onSuccess(
                                                                entity: MutableList<Int>,
                                                                flag: String
                                                            ) {
                                                                if (entity.isEmpty()) {  // 没有座位
                                                                    alert("哎呀，没有可用的座位了！") {
                                                                        positiveButton("修改时间段") { }
                                                                    }.show()
                                                                } else { // 有边上有空的座位
                                                                    Intent(this@Book, SeatInfoActivity::class.java).apply {
                                                                        putExtra("start", start)
                                                                        putExtra("end", end)
                                                                        putExtra("userID", userID)
                                                                        putExtra("now_seat", now_seat)
                                                                        putExtra("subject", sub)
                                                                        if (g != null) putExtra("targetgender", g)
                                                                        if (sg != null) putExtra("selfgender", sg)
                                                                        putExtra("list", entity.toIntArray())
                                                                        putExtra("wait", true)
                                                                        startActivity(this)
                                                                    }
                                                                    finish()
                                                                }
                                                            }
                                                        })
                                                }
                                                else {
                                                    Log.w("bobbob", "interesting")
                                                    val intent = Intent(this@Book, SeatInfoActivity::class.java)
                                                    val bundle = Bundle()
                                                    bundle.putSerializable("order", entity as Serializable)
                                                    intent.putExtra("pair", true)
                                                    intent.putExtra("start", start)
                                                    intent.putExtra("end", end)
                                                    intent.putExtra("userID", userID)
                                                    intent.putExtra("now_seat", now_seat)
                                                    intent.putExtra("subject", sub)
                                                    if (g != null) intent.putExtra("targetgender", g)
                                                    if (sg != null) intent.putExtra("selfgender", sg)
                                                    intent.putExtra("list", entity.keys.toIntArray())
                                                    //    val gson = Gson()
                                                    //    val order = gson.toJson(entity)
                                                    intent.putExtras(bundle)
                                                    startActivity(intent)
                                                }
                                                finish()
                                            }
                                        })
                                }
                            })
                    }

                }
            }

        }

        // 取消
        book_back.setOnClickListener { finish() }


    }

}