package com.example.librarymanagement.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.adapter.OrderDao
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.adapter.TeacherDao
import com.example.librarymanagement.model.*

@Database(entities = [Student::class, Teacher::class, Order::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getStudentDao(): StudentDao

    abstract fun getTeacherDao(): TeacherDao

    abstract fun getOrderDao(): OrderDao

    companion object {

        val instance = Single.sin

    }

    private object Single {

        val sin :AppDataBase= Room.databaseBuilder(
            MyApplication.instance(),
            AppDataBase::class.java,
            "User1.db"
        )
            .allowMainThreadQueries()
            .build()
    }

}
