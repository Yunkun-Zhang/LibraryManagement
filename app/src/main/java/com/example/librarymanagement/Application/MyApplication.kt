package com.example.librarymanagement.Application

import android.app.Application
import androidx.multidex.MultiDex
import com.example.librarymanagement.control.IMcontroler
import com.example.librarymanagement.control.SeatControl
import com.stormkid.okhttpkt.core.Okkt
import kotlin.properties.Delegates

class MyApplication: Application() {
    var seatControl:SeatControl = SeatControl()
    // var orderControl: OrderControl = OrderControl()
    companion object {

        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance


    }
    override fun onCreate() {
        super.onCreate()
        //multiDex
        MultiDex.install(this)
        seatControl = SeatControl()
        instance = this


        //val oDao: OrderDao = AppDataBase.instance.getOrderDao()
        // val uDao: UserDao = AppDataBase.instance.getUserDao()
        //var seatControl: SeatControl = SeatControl()
        // var orderControl: OrderControl = OrderControl()

        // testing http connection
        /*
        var s_1 = Order(1, 1, 1101,"5",8,13,false,false,"English", true)
        var s_2 = Order(2, 5, 1204,"4",10,14,false,false,"Maths", true)
        var s_3 = Order(3, 7, 2083,"9",8,17,false,false,"Politics", false)

        var sList: MutableList<Order> = mutableListOf<Order>()

        sList.add(s_1)
        sList.add(s_2)
        sList.add(s_3)

        //可以直接把list传进去，也可以一个一个单独添加
        oDao.insertAll(sList)*/

        //初始化融云
        IMcontroler().init(this)

        //初始化连接服务器
        Okkt.instance
            .setBase("http://192.168.1.5:8080")
            .isLogShow(true)
            .setErr("Bad Internet Connection!")
            .setClientType(Okkt.FACTORY_CLIENT)
            .setNetClientType(Okkt.HTTP_TYPE)
            .setTimeOut(1000L)
            .isNeedCookie(false)
            .initHttpClient()
    }
}