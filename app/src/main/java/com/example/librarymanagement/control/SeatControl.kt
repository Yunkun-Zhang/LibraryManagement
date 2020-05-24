package com.example.librarymanagement.control

import com.example.librarymanagement.entity.Seat
import kotlin.math.abs

class SeatControl {
    //全部座位，可变集合
    var allSeats:MutableSet<Int> = mutableSetOf()
    init {
        for (floor in 1..4) {
            for (table in 1..20) {
                for (seat in 1..4) {
                    var seatID = floor*1000 + table*10 + seat
                    allSeats.add(seatID)
                }
            }
        }
    }

    var seatBookStatus: MutableList<MutableSet<Int>> = mutableListOf(mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf())

    fun setSeatBooked(seat:Seat, startTime:Int, endTime:Int) {
        for (s in startTime until endTime) {
            seatBookStatus[s-8].add(seat.seatId)
        }
    }

    fun querySeatByTime(seatID: Int, startTime:Int, endTime:Int){
        var s = seatBookStatus[startTime-8].union(seatBookStatus[startTime-7]).toMutableSet()
        for (index in (startTime-7) until (endTime-8)) {
            s = s.union(seatBookStatus[index]).toMutableSet()
        }
        var resultSet = allSeats.subtract(s)
    }

    fun findAdjacent(seatID:Int, startTime: Int, endTime: Int): Int {
        var adjacentSeatID:Int = 0
        adjacentSeatID = if (seatID % 2 == 1) seatID + 1 else seatID - 1


    }


}