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
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import java.util.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val userID = intent.getIntExtra("userID", 0)
        val name = intent.getStringExtra("name")

        // 返回按钮
        btn_back.setOnClickListener { finish() }

        Okkt.instance.Builder().setUrl("/reservation/findbyuserid")
            .setParams(hashMapOf("userid" to userID.toString()))
            .post(object: CallbackRule<MutableList<Reservation>> {
                override suspend fun onFailed(error: String) {
                    val alertDialog = AlertDialog.Builder(this@OrderActivity)
                    alertDialog.setTitle("获取订单列表失败")
                    alertDialog.setMessage(error)
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }

                override suspend fun onSuccess(entity: MutableList<Reservation>, flag: String) {
                    val recyclerView: RecyclerView = find(R.id.order_list)
                    val list = arrayListOf<String>()
                    for (i in entity) {
                        list.add(i.ordertime)
                    }
                    val layoutManager = LinearLayoutManager(this@OrderActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    // layoutManager
                    recyclerView.layoutManager = layoutManager
                    // setAdapter
                    val adapter = OrderList(this@OrderActivity, list)
                    friend_list.adapter = adapter
                    // itemClick
                    adapter.setOnKotlinItemClickListener(object : OrderList.IKotlinItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            // toast("点击了$position")
                            // 发送好友申请
                            Okkt.instance.Builder().setUrl("/user/findbyuserid")
                                .putBody(hashMapOf("userid" to userID.toString())).post(object : CallbackRule<User> {
                                    override suspend fun onFailed(error: String) {}
                                    override suspend fun onSuccess(user: User, flag: String) {
                                        // 获取好友
                                        Okkt.instance.Builder().setUrl("/invite/add")
                                            .setParams(
                                                hashMapOf(
                                                    "sendID" to userID.toString(),
                                                    "sendname" to user.name,
                                                    "receivedID" to entity[position].companion.toString()
                                                )
                                            )
                                            .post(object : CallbackRule<MutableList<Reservation>> {
                                                override suspend fun onFailed(error: String) {}

                                                override suspend fun onSuccess(
                                                    entity: MutableList<Reservation>,
                                                    flag: String
                                                ) {
                                                    alert("成功发送好友申请！") {}.show()
                                                }
                                            })
                                    }
                                })
                        }
                    })

                }
            })
    }

}