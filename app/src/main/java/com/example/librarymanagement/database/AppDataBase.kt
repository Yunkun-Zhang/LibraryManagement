package com.example.librarymanagement.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.adapter.*
import com.example.librarymanagement.model.*

@Database(entities = [User::class, Order::class, Seat::class], version = 1)
abstract class AppDataBase : RoomDatabase() {


    abstract fun getUserDao(): UserDao

    abstract fun getOrderDao(): OrderDao

    abstract fun getSeatDao(): SeatDao


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
