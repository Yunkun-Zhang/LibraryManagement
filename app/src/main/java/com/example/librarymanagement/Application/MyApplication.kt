package com.example.librarymanagement.Application

import android.app.Application
import com.example.librarymanagement.adapter.OrderDao
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.model.Order
import com.example.librarymanagement.model.Student
import kotlin.properties.Delegates

class MyApplication: Application() {
    companion object {

        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance


    }
    override fun onCreate() {
        super.onCreate()

        instance = this
        val oDao: OrderDao = AppDataBase.instance.getOrderDao()

        var s_1 = Order(1, "s1", "小学","","",false,"",null)
        var s_2 = Order(2, "s2", "小学","","",false,"",null)
        var s_3 = Order(3, "s3", "小学","","",false,"",null)
        var s_6 = Order(6, "s6", "大学","","",false,"",null)
        var s_5 = Order(5, "s5", "大学","","",false,"",null)
        var s_4 = Order(4, "s4", "大学","","",false,"",null)

        var sList: MutableList<Order> = mutableListOf<Order>()

        sList.add(s_1)
        sList.add(s_2)
        sList.add(s_3)
        sList.add(s_6)
        sList.add(s_5)
        sList.add(s_4)

        //可以直接把list传进去，也可以一个一个单独添加
        oDao.insertAll(sList)
    }

}