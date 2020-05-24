package com.example.librarymanagement.control

import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.entity.Seat

class SeatControl {

    //val sDao = AppDataBase.instance.getSeatDao()
    //全部座位，可变集合
    var allSeats:MutableSet<Int> = mutableSetOf()
    init {
        for (floor in 1..4) {
            for (table in 1..20) {
                for (seat in 1..4) {
                        allSeats.add((floor*1000 + table*10 + seat))
                }
            }
        }
    }

    var seatBookStatus: MutableList<MutableSet<Int>> = mutableListOf(mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf())

    fun setSeatBooked(seatID: Int, startTime:Int, endTime:Int) {
        for (s in startTime until endTime) {
            seatBookStatus[s-8].add(seatID) //-8 indicates 8am is index 0 in the list
            /* var start = sDao.getSeatID(seatID).orderStartTime
            var end = sDao.getSeatID(seatID).orderEndTime
            start += startTime.toString()
            end += endTime.toString()
            sDao.update(
                com.example.librarymanagement.model.Seat(
                    seatID = seatID,
                    seatStatus = null,
                    isFree = false,
                    orderStartTime = start,
                    orderEndTime = end
                )
            ) */
        }
    }

    fun checkSeatStatusWhenBook(seatID: Int, startTime:Int, endTime:Int):Boolean {
        val s = querySeatByTime(startTime, endTime)
        return seatID in s
    }

    fun querySeatByTime(startTime:Int, endTime:Int): Set<Int>{
        var s = seatBookStatus[startTime-8].union(seatBookStatus[startTime-7]).toMutableSet()
        for (index in (startTime-7) until (endTime-8)) {
            s = s.union(seatBookStatus[index]).toMutableSet()
        }
        return allSeats.subtract(s)
    }

    fun findAdjacent(seatID:Int, startTime: Int, endTime: Int): Int {
        val adjacentSeatID:Int = if (seatID % 2 == 1) seatID + 1 else seatID - 1
        return if (checkSeatStatusWhenBook(adjacentSeatID, startTime, endTime)) adjacentSeatID else 0
    }


}