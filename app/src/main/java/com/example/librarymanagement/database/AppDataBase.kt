package com.example.librarymanagement.database

import androidx.room.*
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.adapter.*
import com.example.librarymanagement.model.*

@Database(entities = [Order::class], version = 1)
abstract class AppDataBase : RoomDatabase() {


    abstract fun getOrderDao(): OrderDao


    companion object {

        val instance = Single.sin

    }

    private object Single {

        val sin :AppDataBase= Room.databaseBuilder(
            MyApplication.instance(),
            AppDataBase::class.java,
            "12"
        )
            .allowMainThreadQueries()
            .build()
    }

}
