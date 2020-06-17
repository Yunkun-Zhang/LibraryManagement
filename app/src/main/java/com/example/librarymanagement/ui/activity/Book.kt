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
import com.example.librarymanagement.control.OrderControl
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


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // 进入该页面前一定处于登录状态
        var userID = intent.getIntExtra("userID", 0)
        var seatID = intent.getIntExtra("seatID", 0)
        val orderID = intent.getIntExtra("orderID", 0)

        //start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
        //end_time.text = Editable.Factory.getInstance().newEditable(end.toString())
        if (seatID != 0) seat.text = seatID.toString()

        if (orderID != 0) {
            // 获取订单信息，如start=13,end=18
            //start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
            //end_time.text = Editable.Factory.getInstance().newEditable(end.toString())
            start_time.isEnabled = false
            end_time.isEnabled = false
            choose_seat.isEnabled = false
            subject.isEnabled = false
            gender.isEnabled = false
            cancel.isEnabled = true
            cancel.setOnClickListener {
                // 加入取消订单操作*****************************
                Intent(this, MainActivity::class.java).apply {
                    putExtra("orderID", 0)
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
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
                    val sub = subject.text.toString()
                    val g: Boolean?

                    if (gender.selectedItem == "男") g = true
                    else if (gender.selectedItem == "女") g = false
                    else g = null

                    if (sub == "" && g == null) {// 没有匹配信息
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
                                            //putExtra("subject", sub)
                                            //putExtra("gender", g)
                                            putExtra("list", entity.toIntArray())
                                            startActivity(this)
                                        }
                                    }
                                }
                            })
                    }
                    else {
                        Okkt.instance.Builder().setUrl("/user/findbyuserid")
                            .putBody(hashMapOf("userid" to userID.toString())).post(object : CallbackRule<User> {
                            override suspend fun onFailed(error: String) {
                                toast("failed")
                            }

                            override suspend fun onSuccess(entity: User, flag: String) {
                                val sg = entity.gender
                                Okkt.instance.Builder().setUrl("/seatwithreservation/findseatwithadjacentreservation?starttime=8&endtime=13&subject=soft")
                                    .putBody(
                                        hashMapOf(
                                            "starttime" to start.toString(), "endtime" to end.toString(),
                                            "targetgender" to g.toString(), "selfgender" to sg.toString(),
                                            "subject" to sub.toString()
                                        )
                                    )
                                    .post(object : CallbackRule<HashMap<Int, List<Int>>> {
                                        override suspend fun onFailed(error: String) { Log.w("bobbob", "failed") }
                                        override suspend fun onSuccess(entity: HashMap<Int, List<Int>>, flag: String) {
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
                                                                    putExtra("subject", sub)
                                                                    putExtra("targetgender", g)
                                                                    putExtra("list", entity.toIntArray())
                                                                    startActivity(this)
                                                                }
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
                                                intent.putExtra("subject", sub)
                                                intent.putExtra("targetender", g)
                                                intent.putExtra("list", entity.keys.toIntArray())
                                                //    val gson = Gson()
                                                //    val order = gson.toJson(entity)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
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