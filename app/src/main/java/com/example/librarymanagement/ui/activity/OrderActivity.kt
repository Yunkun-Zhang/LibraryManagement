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
                    for (i in entity) {
                        dates.add(i.ordertime)
                    }
                    val layoutManager = LinearLayoutManager(this@OrderActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    // layoutManager
                    recyclerView.layoutManager = layoutManager
                    // setAdapter
                    val adapter = OrderList(this@OrderActivity, dates)
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
                                        textView("预订时间：${someone.ordertime}")
                                        textView("开始时间：${someone.starttime}")
                                        textView("结束时间：${someone.endtime}")
                                        textView("选择座位：${someone.seatID}")
                                        if (someone.subject != null) textView("配对科目：${someone.subject}")
                                        if (someone.companion == null) textView("和谁一起：自己占座")
                                        else textView("和谁一起：用户 ${someone.companion} （点击查看信息）") {
                                            setOnClickListener {
                                                Intent(this@OrderActivity, PersonInfoActivity::class.java).apply {
                                                    putExtra("friend", false)
                                                    putExtra("userID", userID)
                                                    putExtra("friendid", someone.companion)
                                                    startActivity(this)
                                                }
                                            }
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

}