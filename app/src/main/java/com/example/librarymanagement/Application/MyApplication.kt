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
        val sDao: StudentDao = AppDataBase.instance.getStudentDao()

        var s_1 = Student(1, "s1", "小学")
        var s_2 = Student(2, "s2", "小学")
        var s_3 = Student(3, "s3", "小学")
        var s_6 = Student(6, "s6", "大学")
        var s_5 = Student(5, "s5", "大学")
        var s_4 = Student(4, "s4", "大学")

        var sList: MutableList<Student> = mutableListOf<Student>()

        sList.add(s_1)
        sList.add(s_2)
        sList.add(s_3)
        sList.add(s_6)
        sList.add(s_5)
        sList.add(s_4)

        //可以直接把list传进去，也可以一个一个单独添加
        sDao.insertAll(sList)

        println(sDao.getStudent(1))

    }

}