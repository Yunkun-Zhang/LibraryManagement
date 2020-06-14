package com.example.librarymanagement.httputil

import android.util.Log
import com.example.librarymanagement.model.Users
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.TestCallbackRule


fun getAllUsers(){
    Okkt.instance.Builder().setUrl("/user/all").get(object: CallbackRule<MutableList<Users>>{
        override suspend fun onFailed(error: String) {
        }

        override suspend fun onSuccess(entity: MutableList<Users>, flag: String) {
            Log.w("response", entity[0].name)
        }

    })
}


fun addUser(user: Users) {
    Okkt.instance.Builder().setUrl("/user/add").putBody(hashMapOf("name" to user.name, "psd" to user.password)).
    post(object:CallbackRule<Users> {
        override suspend fun onFailed(error: String) {
        }

        override suspend fun onSuccess(entity: Users, flag: String) {
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