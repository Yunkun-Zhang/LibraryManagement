package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.librarymanagement.extension.SeatStatus

@Entity(tableName = "Seat")
data class Seat(

    @PrimaryKey(autoGenerate = true)
    var seatID: Int,
    @ColumnInfo(name = "seat_status")
    var seatStatus: SeatStatus?,
    @ColumnInfo(name = "is_free")
    var isFree: Boolean,
    @ColumnInfo(name = "order_period_today")
    var orderPeriodToday: MutableList<MutableList<Int>>?,
    @ColumnInfo(name = "order_period_tomorrow")
    var orderPeriodTomorrow: MutableList<MutableList<Int>>?

)
