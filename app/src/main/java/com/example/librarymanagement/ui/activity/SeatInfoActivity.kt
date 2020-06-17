package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.model.User
import com.example.librarymanagement.others.UserStatus
import com.google.gson.Gson
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_seat_info.*
import org.jetbrains.anko.*
import splitties.views.dsl.core.lParams
import splitties.views.gravityCenter
import android.util.Log
import com.stormkid.okhttpkt.rule.StringCallback

class SeatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val check = intent.getBooleanExtra("check", false)
        val hour = intent.getIntExtra("hour", 0)
        val userID = intent.getIntExtra("userID", 0)
        val start = intent.getIntExtra("start", 8)
        val end = intent.getIntExtra("end", 23)
        var seatID = 0
        var sub = intent.getStringExtra("subject")
        val tg : Boolean? = intent.extras?.get("targetgender") as Boolean?
        val pair = intent.getBooleanExtra("pair", false)
        var wait = intent.getBooleanExtra("wait", false)
        // 获取可用座位list
        val seat_list = intent.getIntArrayExtra("list")


        if (check) {
            // 当前可用的seat_list
            toast("现在是$hour")
        }

        // 布局
        verticalLayout {
            include<View>(R.layout.activity_seat_info) {}.lparams(height = wrapContent, width = matchParent)

            relativeLayout {
                for (floor in 1..3) {
                    horizontalScrollView {
                        id = floor
                        this.setBackgroundResource(R.drawable.frame)
                        topPadding = dip(10)
                        // 以下是所有座位
                        verticalLayout {
                            for (line in 0..3) {
                                linearLayout {
                                    for (col in 1..12) {
                                        linearLayout {
                                            gravity = gravityCenter
                                            horizontalPadding = dip(20)
                                            verticalPadding = dip(5)
                                            // 以下是一桌：左侧两位，右侧两位
                                            relativeLayout {
                                                imageView {
                                                    id = floor * 1000 + line * 120 + col * 10 + 1
                                                    if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                                    else this.setImageResource(R.drawable.shape_red)
                                                    setOnClickListener {
                                                        if (this.id in seat_list) {

                                                            Log.w("checkid", this.id.toString())
                                                            if (seatID != this.id) {
                                                                if (seatID != 0) toast("只能选择一个座位！")
                                                                else {
                                                                    this.setImageResource(R.drawable.shape_chosen)
                                                                    seatID = this.id
                                                                }
                                                            } else {
                                                                this.setImageResource(R.drawable.shape_green)
                                                                seatID = 0
                                                            }
                                                        } else toast("该座位已被占用！")
                                                    }
                                                }.lparams { topMargin = dip(10) }
                                                imageView {
                                                    id = floor * 1000 + line * 120 + col * 10 + 2
                                                    if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                                    else this.setImageResource(R.drawable.shape_red)
                                                    setOnClickListener {
                                                        if (this.id in seat_list) {
                                                            if (seatID != this.id) {
                                                                if (seatID != 0) toast("只能选择一个座位！")
                                                                else {
                                                                    this.setImageResource(R.drawable.shape_chosen)
                                                                    seatID = this.id
                                                                }
                                                            } else {
                                                                this.setImageResource(R.drawable.shape_green)
                                                                seatID = 0
                                                            }
                                                        } else toast("该座位已被占用！")
                                                    }
                                                }.lparams {
                                                    alignParentBottom()
                                                    bottomMargin = dip(10)
                                                }
                                            }.lparams(width = wrapContent, height = matchParent)

                                            button {
                                                backgroundResource = R.drawable.rect
                                                isClickable = false
                                                text = "${line * 12 + col}"
                                            }.lparams(width = dip(40), height = dip(60)) {
                                                horizontalMargin = dip(10)
                                            }

                                            relativeLayout {
                                                imageView {
                                                    id = floor * 1000 + line * 120 + col * 10 + 3
                                                    if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                                    else this.setImageResource(R.drawable.shape_red)
                                                    setOnClickListener {
                                                        if (this.id in seat_list) {
                                                            if (seatID != this.id) {
                                                                if (seatID != 0) toast("只能选择一个座位！")
                                                                else {
                                                                    this.setImageResource(R.drawable.shape_chosen)
                                                                    seatID = this.id
                                                                }
                                                            } else {
                                                                this.setImageResource(R.drawable.shape_green)
                                                                seatID = 0
                                                            }
                                                        } else toast("该座位已被占用！")
                                                    }
                                                }.lparams { topMargin = dip(10) }
                                                imageView {
                                                    id = floor * 1000 + line * 120 + col * 10 + 4
                                                    if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                                    else this.setImageResource(R.drawable.shape_red)
                                                    setOnClickListener {
                                                        if (this.id in seat_list) {
                                                            if (seatID != this.id) {
                                                                if (seatID != 0) toast("只能选择一个座位！")
                                                                else {
                                                                    this.setImageResource(R.drawable.shape_chosen)
                                                                    seatID = this.id
                                                                }
                                                            } else {
                                                                this.setImageResource(R.drawable.shape_green)
                                                                seatID = 0
                                                            }
                                                        } else toast("该座位已被占用！")
                                                    }
                                                }.lparams {
                                                    alignParentBottom()
                                                    bottomMargin = dip(10)
                                                }
                                            }.lparams(width = wrapContent, height = matchParent)
                                        }
                                    }
                                }.lparams {
                                    topMargin = dip(5)
                                    if (line == 1) bottomMargin = dip(40)
                                }
                            }
                        }.lparams(height = matchParent)

                    }.lparams(width = matchParent, height = dip(360)) {
                        margin = dip(20)
                    }
                }

                textView("确定") {
                    id = R.id.confirm
                    textSize = 20f
                    textColor = Color.parseColor("#87e3cf")
                }.lparams {
                    leftMargin = dip(20)
                    bottomMargin = dip(20)
                    alignParentBottom()
                }
            }.lparams(width = matchParent, height = matchParent)

        }.lParams(width = matchParent, height = matchParent)

        // 确定
        val conf = findViewById<TextView>(R.id.confirm)
        if (hour in 8..22) conf.visibility = View.GONE
        else {
            conf.setOnClickListener {
                // 跳转回主界面，传递seatID，userID，orderID
                // 先找到user
                Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userid" to userID.toString())).
                post(object: CallbackRule<User> {
                    override suspend fun onFailed(error: String) { toast("failed") }
                    override suspend fun onSuccess(entity: User, flag: String) {
                        // 开始生成reservation

                        if (pair) {
                            if (!wait) {
                                val order = intent.getSerializableExtra("order") as HashMap<Int, List<Int>>
                                val map: HashMap<String, String> = hashMapOf(
                                    "userid" to userID.toString(),
                                    "seatid" to seatID.toString(),
                                    "starttime" to start.toString(),
                                    "endtime" to end.toString(),
                                    "pair" to pair.toString(),
                                    "hang" to false.toString()
                                )
                                if (tg != null) map["targetgender"] = tg.toString()
                                if (entity.gender != null) map["selfgender"] = entity.gender.toString()
                                if (order[seatID]!![0] != null) map["companion"] = order[seatID]!![0].toString()
                                Okkt.instance.Builder().setUrl("/reservation/add")
                                    .putBody(map)
                                    .post(object : CallbackRule<Int> {
                                        override suspend fun onFailed(error: String) {}
                                        override suspend fun onSuccess(entity: Int, flag: String) {
                                            alert("预订成功！") { positiveButton("确定") {} }.show()
                                            Okkt.instance.Builder().setUrl("/seat/setseat/book").putBody(
                                                hashMapOf(
                                                    "seatid" to seatID.toString(),
                                                    "starttime" to start.toString(),
                                                    "endtime" to end.toString()
                                                )
                                            ).post(object : CallbackRule<String> {
                                                override suspend fun onFailed(error: String) {}
                                                override suspend fun onSuccess(entity: String, flag: String) {}
                                            })
                                            Okkt.instance.Builder().setUrl("/reservation/release")
                                                .setParams(
                                                    hashMapOf(
                                                        "reservationid" to order[seatID]!![1].toString(),
                                                        "companion" to userID.toString()
                                                    )
                                                ).post(object : StringCallback {
                                                    override suspend fun onFailed(error: String) {}
                                                    override suspend fun onSuccess(entity: String, flag: String) {}
                                                })

                                            Intent(this@SeatInfoActivity, MainActivity::class.java).apply {
                                                putExtra("seat", seatID)
                                                putExtra("userID", userID)
                                                putExtra("orderID", entity)
                                                startActivity(this)
                                            }
                                        }
                                    })
                            }
                            // 没有找到合适的配对
                            else {
                            val map: HashMap<String, String> = hashMapOf(
                                "userid" to userID.toString(),
                                "seatid" to seatID.toString(),
                                "starttime" to start.toString(),
                                "endtime" to end.toString(),
                                "pair" to pair.toString()
                            )
                            if (tg != null) map["targetgender"] = tg.toString()
                            if (entity.gender != null) map["selfgender"] = entity.gender.toString()
                                Okkt.instance.Builder().setUrl("/reservation/add").putBody(map)
                                    .post(object : CallbackRule<Int> {
                                        override suspend fun onFailed(error: String) {}
                                        override suspend fun onSuccess(entity: Int, flag: String) {
                                            alert("预订成功！") { positiveButton("确定") {} }.show()
                                            Okkt.instance.Builder().setUrl("/seat/setseat/book").putBody(
                                                hashMapOf(
                                                    "seatid" to seatID.toString(),
                                                    "starttime" to start.toString(),
                                                    "endtime" to end.toString()
                                                )
                                            ).post(object : CallbackRule<String> {
                                                override suspend fun onFailed(error: String) {}
                                                override suspend fun onSuccess(entity: String, flag: String) {}
                                            })
                                            Intent(this@SeatInfoActivity, MainActivity::class.java).apply {
                                                putExtra("seat", seatID)
                                                putExtra("userID", userID)
                                                putExtra("orderID", entity)
                                                startActivity(this)
                                            }
                                        }
                                    })

                                }
                            }
                        else {// 无配对
                            Okkt.instance.Builder().setUrl("/reservation/add")
                                .putBody(
                                    hashMapOf(
                                        "userid" to userID.toString(),
                                        "seatid" to seatID.toString(),
                                        "starttime" to start.toString(),
                                        "endtime" to end.toString(),
                                        "pair" to pair.toString(),
                                        "hang" to false.toString())
                                )
                                .post(object : CallbackRule<Int> {
                                    override suspend fun onFailed(error: String) {
                                        alert("预订失败！") { positiveButton("确定") {} }.show()
                                    }
                                    override suspend fun onSuccess(entity: Int, flag: String) {
                                        alert("预订成功！") { positiveButton("确定") {} }.show()
                                        // 更改座位信息
                                        Okkt.instance.Builder().setUrl("/seat/setseat/book").
                                        putBody(hashMapOf("seatid" to seatID.toString(),
                                            "starttime" to start.toString(),
                                            "endtime" to end.toString())).
                                        post(object: CallbackRule<String> {
                                            override suspend fun onFailed(error: String) {}
                                            override suspend fun onSuccess(entity: String, flag: String) { }
                                        })

                                        Intent(this@SeatInfoActivity, MainActivity::class.java).apply {
                                            putExtra("seat", seatID)
                                            putExtra("userID", userID)
                                            putExtra("orderID", entity)
                                            startActivity(this)
                                        }
                                    }
                                })
                        }
                    }
                })

            }
        }

        // 返回按钮
        btn_back.setOnClickListener {
            finish()
        }

        // 楼层
        val a = findViewById<HorizontalScrollView>(1)
        val b = findViewById<HorizontalScrollView>(2)
        val c = findViewById<HorizontalScrollView>(3)
        b.visibility = View.GONE
        c.visibility = View.GONE
        btn_a.setOnClickListener {
            a.visibility = View.VISIBLE
            a.isEnabled = false
            b.visibility = View.GONE
            c.visibility = View.GONE
        }
        btn_b.setOnClickListener {
            a.visibility = View.GONE
            b.visibility = View.VISIBLE
            b.isEnabled = false
            c.visibility = View.GONE
        }
        btn_c.setOnClickListener {
            a.visibility = View.GONE
            b.visibility = View.GONE
            c.visibility = View.VISIBLE
            c.isEnabled = false
        }
    }
}
