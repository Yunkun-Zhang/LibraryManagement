package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class Order(

    @PrimaryKey(autoGenerate = true)
    var orderID: Int?,
    @ColumnInfo(name = "order_name")
    var userID: Int?,
    @ColumnInfo(name = "order_name")
    var ordertime: Int?,
    @ColumnInfo(name = "begin_time")
    var begintime: Int?,
    @ColumnInfo(name = "end_time")
    var endtime: Int?,
    @ColumnInfo(name = "order_status")
    var orderstatus:Boolean, //finished or not
    @ColumnInfo(name = "pair_status")
    var pairstatus:Boolean?,
    @ColumnInfo(name = "subject")
    var subject:String?,
    @ColumnInfo(name = "gender")
    var gender:Boolean?

)