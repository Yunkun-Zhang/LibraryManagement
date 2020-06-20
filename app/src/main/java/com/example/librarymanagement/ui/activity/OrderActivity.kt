package com.example.librarymanagement.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.OrderList
import com.example.librarymanagement.model.Reservation
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_friend.*
import kotlinx.android.synthetic.main.activity_friend.btn_back
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.*
import splitties.views.dsl.core.lParams
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val userID = intent.getIntExtra("userID", 0)

        // 返回按钮
        btn_back.setOnClickListener { finish() }

        // 获取订单列表
        Okkt.instance.Builder().setUrl("/reservation/findbyuserid")
            .setParams(hashMapOf("userid" to userID.toString()))
            .get(object: CallbackRule<MutableList<Reservation>> {
                override suspend fun onFailed(error: String) {
                    alert(error) {
                        positiveButton("确定") {}
                        setTitle("获取订单列表失败")
                    }.show()
                }
                override suspend fun onSuccess(entity: MutableList<Reservation>, flag: String) {
                    val recyclerView: RecyclerView = find(R.id.order_list)
                    val dates = arrayListOf<String>()
                    val status = arrayListOf<String>()
                    var calendar: Calendar = Calendar.getInstance()
                    for (i in entity) {
                        var date: Date = SimpleDateFormat("yyyy-MM-dd").parse(i.ordertime)
                        calendar.time = date
                        var day = calendar.get(Calendar.DATE)
                        calendar.set(Calendar.DATE, day + 1)
                        val real = SimpleDateFormat("yyyy-MM-dd ${i.starttime}:00-${i.endtime}:00")
                        dates.add(real.format(calendar.time))
                        if (isLater(i.ordertime, i.endtime)) status.add("已结束")
                        else if (isLater(i.ordertime, i.starttime)) status.add("进行中")
                        else status.add("未开始")
                    }
                    val layoutManager = LinearLayoutManager(this@OrderActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    // layoutManager
                    recyclerView.layoutManager = layoutManager
                    // setAdapter
                    val adapter = OrderList(this@OrderActivity, dates, status)
                    order_list.adapter = adapter
                    // itemClick
                    adapter.setOnKotlinItemClickListener(object : OrderList.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            // toast("点击了$position")
                            val someone = entity[position]
                            alert {
                                customView{
                                    verticalLayout {
                                        padding = dip(20)
                                        textView("订单编号：${someone.reservationid}")
                                        textView("下单日期：${someone.ordertime}")
                                        textView("开始时间：${someone.starttime}")
                                        textView("结束时间：${someone.endtime}")
                                        textView("选择座位：${someone.seatID}")
                                        if (someone.subject != null && someone.subject != "") textView("配对科目：${someone.subject}")
                                        if (someone.targetgender == true) textView("配对性别：男生")
                                        else if (someone.targetgender == false) textView("配对性别：女生")
                                        if (someone.companion == null) textView("和谁一起：自己占座")
                                        else {
                                            // 判断订单是否结束
                                            if (isLater(someone.ordertime, someone.endtime)) {
                                                textView("和谁一起：用户 ${someone.companion} （点击查看信息）") {
                                                    setOnClickListener {
                                                        Intent(
                                                            this@OrderActivity,
                                                            PersonInfoActivity::class.java
                                                        ).apply {
                                                            putExtra("friend", false)
                                                            putExtra("userID", userID)
                                                            putExtra("friendid", someone.companion)
                                                            startActivity(this)
                                                        }
                                                    }
                                                }
                                            }
                                            else textView("和谁一起：某位有缘人")
                                        }
                                    }.lParams(width = wrapContent, height = wrapContent)
                                    positiveButton("确定") {}
                                }
                            }.show()
                        }
                    })

                }
            })
    }

    fun isLater(d: String, e: Int): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        val t = "${d} $e:00:00"
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        calendar.time = sdf.parse(t)
        val day = calendar.get(Calendar.DATE)
        calendar.set(Calendar.DATE, day+1)
        val now = sdf.parse(sdf.format(Date())).time
        val tt = sdf.parse(sdf.format(calendar.time)).time
        return tt <= now
    }

}