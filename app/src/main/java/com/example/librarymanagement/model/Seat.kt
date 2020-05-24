package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Seat")
data class Seat(

    @PrimaryKey(autoGenerate = true)
    var seatID: Int,
    @ColumnInfo(name = "seat_status")
    var seatStatus: String?,
    @ColumnInfo(name = "is_free")
    var isFree: Boolean,
    @ColumnInfo(name = "order_start_time")
    var orderStartTime: String?,
    @ColumnInfo(name = "order_end_time")
    var orderEndTime: String?

)
