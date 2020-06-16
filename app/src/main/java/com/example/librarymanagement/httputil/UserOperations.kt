package com.example.librarymanagement.httputil

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.TestCallbackRule

class UserOperations{

    val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    fun init(){
        Okkt.instance
            .setBase("http://192.168.3.161:8080")
            .isLogShow(true)
            .setErr("Bad Internet Connection!")
            .setClientType(Okkt.FACTORY_CLIENT)
            .setNetClientType(Okkt.HTTP_TYPE)
            .setTimeOut(1000L)
            .isNeedCookie(false)
            .initHttpClient()
    }


    fun getAllUsers(){
        Okkt.instance.Builder().setUrl("/user/all").get(object: CallbackRule<MutableList<User>>{
            override suspend fun onFailed(error: String) {
                Log.w("response",error)
            }

            override suspend fun onSuccess(entity: MutableList<User>, flag: String) {
                Log.w("response", entity[0].name)
                val message: Message = handler.obtainMessage()
                message.what = 1
                message.obj = entity[0].name
                handler.sendMessage(message)
            }

        })
    }


    fun addUser(user: User) {
        Okkt.instance.Builder().setUrl("/user/add").putBody(hashMapOf("name" to user.name, "psd" to user.password)).
        post(object:CallbackRule<User> {
            override suspend fun onFailed(error: String) {
            }

            override suspend fun onSuccess(entity: User, flag: String) {
                Log.w("result", "success")
            }

        })
    }

    fun doTest(){
        Okkt.instance.TestBuilder().setUrl("http://www.baidu.com").testGet(object : TestCallbackRule{
            override suspend fun onResponse(response: TestCallbackRule.Response) {
                Log.w("response","${response.body.toString()}----${response.heads}")
            }

            override suspend fun onErr(err: String) {
            }

        })
    }

}