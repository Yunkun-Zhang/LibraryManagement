package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class Order(

    @PrimaryKey(autoGenerate = true)
    var orderID: Int,
    @ColumnInfo(name = "user_id")
    var userID: Int,
    @ColumnInfo(name = "seat_id")
    var seatID:Int,
    @ColumnInfo(name = "order_time")
    var orderTime: String,
    @ColumnInfo(name = "begin_time")
    var beginTime: Int,
    @ColumnInfo(name = "end_time")
    var endTime: Int,
    @ColumnInfo(name = "order_status")
    var orderStatus:Boolean, //finished or not
    @ColumnInfo(name = "pair_status")
    var pairStatus:Boolean, //finished or not
    @ColumnInfo(name = "subject")
    var subject:String?, // 科目选择
    @ColumnInfo(name = "gender")
    var gender:Boolean? //true for male

)