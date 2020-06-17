package com.example.librarymanagement.Application

import android.app.Application
import androidx.multidex.MultiDex
import com.example.librarymanagement.control.IMcontroler
import com.stormkid.okhttpkt.core.Okkt
import kotlin.properties.Delegates

class MyApplication: Application() {
    companion object {

        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance


    }
    override fun onCreate() {
        super.onCreate()
        //multiDex
        MultiDex.install(this)
        instance = this

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