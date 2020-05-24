package com.example.librarymanagement.entity

import com.example.librarymanagement.extension.SeatStatus
import java.security.KeyStore

class Seat(val seatID:Int) {
    var isfree:Boolean = true
    var isbooked:Boolean = true
    var status:SeatStatus = SeatStatus.Free
    var orderPeriodToday: MutableList<MutableList<Int>>? = null
    var orderPeriodTomorrow: MutableList<MutableList<Int>>? = null

    fun leave() {
        status = SeatStatus.Leave
        isfree = false
    }

    fun comeback() {
        status = SeatStatus.Occupied
        isfree = false
    }

    fun finish() {
        status = SeatStatus.Free
        isfree = true
    }


}


