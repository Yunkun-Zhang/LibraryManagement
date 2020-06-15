package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_seat_info.*
import org.jetbrains.anko.*
import splitties.views.dsl.core.lParams
import splitties.views.gravityCenter

class SeatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = MyApplication.instance()
        val seatControl = app.seatControl
        val userID = intent.getIntExtra("userID", 0)
        val start = intent.getIntExtra("start", 8)
        val end = intent.getIntExtra("end", 23)
        val floor = intent.getIntExtra("floor", 1)
        var seatID = 0
        // 获取可用座位list
        var seat_list = seatControl.querySeatByTime(start, end)

        verticalLayout {
            include<View>(R.layout.activity_seat_info) {}.lparams(height = wrapContent, width = matchParent)

            horizontalScrollView {
                this.setBackgroundResource(R.drawable.frame)
                // 以下是所有座位
                verticalLayout {
                    for (line in 1..4) {
                        linearLayout {
                            for (col in 1..3) {
                                linearLayout {
                                    gravity = gravityCenter
                                    horizontalPadding = dip(10)
                                    verticalPadding = dip(5)
                                    // 以下是一桌：左侧两位，右侧两位
                                    relativeLayout {
                                        imageView {
                                            id = floor*1000 + line*30 + col*10 - 30 + 1
                                            if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                            else this.setImageResource(R.drawable.shape_red)
                                            setOnClickListener {
                                                if (this.id in seat_list) {
                                                    if (seatID != this.id) {
                                                        if (seatID != 0) toast("只能选择一个座位！")
                                                        else{
                                                            this.setImageResource(R.drawable.shape_chosen)
                                                            seatID = this.id
                                                        }
                                                    }
                                                    else {
                                                        this.setImageResource(R.drawable.shape_green)
                                                        seatID = 0
                                                    }
                                                }
                                                else toast("该座位已被占用！")
                                            }
                                        }.lparams { topMargin = dip(10) }
                                        imageView {
                                            id = floor*1000 + line*30 + col*10 - 30 + 2
                                            if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                            else this.setImageResource(R.drawable.shape_red)
                                            setOnClickListener {
                                                if (this.id in seat_list) {
                                                    if (seatID != this.id) {
                                                        if (seatID != 0) toast("只能选择一个座位！")
                                                        else{
                                                            this.setImageResource(R.drawable.shape_chosen)
                                                            seatID = this.id
                                                        }
                                                    }
                                                    else {
                                                        this.setImageResource(R.drawable.shape_green)
                                                        seatID = 0
                                                    }
                                                }
                                                else toast("该座位已被占用！")
                                            }
                                        }.lparams {
                                            alignParentBottom()
                                            bottomMargin = dip(10) }
                                    }.lparams(width = wrapContent, height = matchParent)

                                    imageView {
                                        backgroundResource = R.drawable.rect
                                    }.lparams(width = dip(40), height = dip(60)) {
                                        horizontalMargin = dip(10) }

                                    relativeLayout {
                                        imageView {
                                            id = floor*1000 + line*30 + col*10 - 30 + 3
                                            if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                            else this.setImageResource(R.drawable.shape_red)
                                            setOnClickListener {
                                                if (this.id in seat_list) {
                                                    if (seatID != this.id) {
                                                        if (seatID != 0) toast("只能选择一个座位！")
                                                        else{
                                                            this.setImageResource(R.drawable.shape_chosen)
                                                            seatID = this.id
                                                        }
                                                    }
                                                    else {
                                                        this.setImageResource(R.drawable.shape_green)
                                                        seatID = 0
                                                    }
                                                }
                                                else toast("该座位已被占用！")
                                            }
                                        }.lparams { topMargin = dip(10) }
                                        imageView {
                                            id = floor*1000 + line*30 + col*10 - 30 + 4
                                            if (this.id in seat_list) this.setImageResource(R.drawable.shape_green)
                                            else this.setImageResource(R.drawable.shape_red)
                                            setOnClickListener {
                                                if (this.id in seat_list) {
                                                    if (seatID != this.id) {
                                                        if (seatID != 0) toast("只能选择一个座位！")
                                                        else{
                                                            this.setImageResource(R.drawable.shape_chosen)
                                                            seatID = this.id
                                                        }
                                                    }
                                                    else {
                                                        this.setImageResource(R.drawable.shape_green)
                                                        seatID = 0
                                                    }
                                                }
                                                else toast("该座位已被占用！")
                                            }
                                        }.lparams {
                                            alignParentBottom()
                                            bottomMargin = dip(10) }
                                    }.lparams(width = wrapContent, height = matchParent)
                                }
                            }
                        }.lparams { verticalMargin = dip(5) }
                    }
                }.lparams(height = matchParent)

            }.lparams(width = matchParent, height = dip(330)) {
                margin = dip(32)
            }

            textView("确定") {
                id = R.id.confirm
                textSize = 20f
                textColor = Color.parseColor("#87e3cf")
            }.lparams {
                leftMargin = dip(20)
                bottomMargin = dip(20)
            }

        }.lParams(width = matchParent, height = matchParent)

        findViewById<TextView>(R.id.confirm).setOnClickListener {
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

        btn_b.setOnClickListener {
            Intent(this, SeatInfoActivity::class.java).apply {
                putExtra("floor", 2)
                putExtra("start", start)
                putExtra("end", end)
                putExtra("seatID", seatID)
                putExtra("userID", userID)
                startActivity(this)
            }
        }
    }
}
