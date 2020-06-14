package com.example.librarymanagement.database

import androidx.room.*
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.adapter.*
import com.example.librarymanagement.model.*

@Database(entities = [User::class, Order::class], version = 1)
abstract class AppDataBase : RoomDatabase() {


    abstract fun getUserDao(): UserDao

    abstract fun getOrderDao(): OrderDao



    companion object {

        val instance = Single.sin

    }

    private object Single {

        val sin :AppDataBase= Room.databaseBuilder(
            MyApplication.instance(),
            AppDataBase::class.java,
            "hhh"
        )
            .allowMainThreadQueries()
            .build()
    }

}
