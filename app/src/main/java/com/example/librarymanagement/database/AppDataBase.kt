package com.example.librarymanagement.database

import androidx.room.*
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.adapter.TeacherDao
import com.example.librarymanagement.model.*

@Database(entities = [Student::class, Teacher::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getStudentDao(): StudentDao

    abstract fun getTeacherDao(): TeacherDao

    companion object {

        val instance = Single.sin

    }

    private object Single {

        val sin :AppDataBase= Room.databaseBuilder(
            MyApplication.instance(),
            AppDataBase::class.java,
            "User.db"
        )
            .allowMainThreadQueries()
            .build()
    }

}
