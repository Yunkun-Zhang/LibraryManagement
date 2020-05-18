package com.example.librarymanagement.Application

import android.app.Application
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.database.AppDataBase
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
    }

}