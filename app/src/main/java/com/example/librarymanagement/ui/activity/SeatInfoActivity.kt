package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.control.OrderControl
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_seat_info.*
import org.jetbrains.anko.*
import splitties.views.dsl.core.lParams
import splitties.views.gravityCenter

class SeatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = MyApplication.instance()
        val seatControl = app.seatControl
        val hour = intent.getIntExtra("hour", 0)
        val userID = intent.getIntExtra("userID", 0)
        val start = intent.getIntExtra("start", 8)
        val end = intent.getIntExtra("end", 23)
        var seatID = 0
        var sub = intent.getStringExtra("subject")
        var gender : Boolean? = intent.extras?.get("gender") as Boolean?
        // 获取可用座位list
        var seat_list = intent.getIntArrayExtra("list")
        if (hour in 8..22) {
            // 当前可用的seat_list
            confirm.visibility = View.GONE
            toast("现在是$hour")
        }
        else {
            if (seat_list.isEmpty()) {
                alert("哎呀，没有可用的匹配座位了！") {
                    positiveButton("放弃匹配") {}
                    negativeButton("返回") { finish() }
                }.show()
                sub = ""
                gender = null
            }
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
        val confirm = findViewById<TextView>(R.id.confirm)
        if (hour in 8..22) confirm.visibility = View.GONE
        else {
            val orderControl = OrderControl()

            confirm.setOnClickListener {
                // 跳转回主界面，传递seatID，userID，orderID

                var valid_seats = orderControl.findPairedSeats(sub, gender, start, end)
                var order = orderControl.create_order(userID, seatID, start, end, false, sub, gender)
                orderControl.confirmOrder(order)

                Intent(this, MainActivity::class.java).apply {
                    putExtra("seat", seatID)
                    putExtra("userID", userID)
                    putExtra("orderID", 1)
                    startActivity(this)
                }

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
