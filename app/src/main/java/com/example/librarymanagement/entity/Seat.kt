package com.example.librarymanagement.entity

import com.example.librarymanagement.extension.SeatStatus
import java.security.KeyStore

class Seat(val seatId:Int) {
    var isfree:Boolean = true

    var status:SeatStatus = SeatStatus.Free

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


