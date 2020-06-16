package com.example.librarymanagement.control


class SeatControl {

    //val sDao = AppDataBase.instance.getSeatDao()
    //全部座位，可变集合
    var allSeats:MutableSet<Int> = mutableSetOf()
    //添加所有座位
    init {
        for (floor in 1..3) {
            for (table in 1..48) {
                for (seat in 1..4) {
                            allSeats.add((floor*1000 + table*10 + seat))
                }
            }
        }
    }


    var seatBookStatus: MutableList<MutableSet<Int>> = mutableListOf(mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),mutableSetOf(),
        mutableSetOf()) //各个时段被占座位的集合的list

    // 将座位设置某时段内被预定
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

    //找到有相邻空座的座位，寻找配对时无结果用的操作
    fun findSeatWithFreeAdjacent(startTime: Int, endTime: Int):MutableList<Int> {
        var seatsWithFreeAdjacent = mutableListOf<Int>()
        val s = querySeatByTime(startTime, endTime)
        for (floor in 1..3) {
            for (table in 1..12) {
                for (seat in 1..4) {
                    var seatID = floor*1000 + table*10 + seat
                    if (seatID % 2 == 0) {
                        if (seatID in s && (seatID-1) in s) {
                            seatsWithFreeAdjacent.add(seatID)
                            seatsWithFreeAdjacent.add(seatID-1)
                        }
                    }
                    else {
                        if (seatID in s && (seatID+1) in s) {
                            seatsWithFreeAdjacent.add(seatID)
                            seatsWithFreeAdjacent.add(seatID+1)
                        }
                    }
                }
            }
        }
        return seatsWithFreeAdjacent
    }

    // 检查特定时间段某座位是否被预定
    fun checkSeatStatusWhenBook(seatID: Int, startTime:Int, endTime:Int):Boolean {
        val s = querySeatByTime(startTime, endTime)
        return seatID in s
    }

    // 特定时段内可预订的座位
    fun querySeatByTime(startTime:Int, endTime:Int): Set<Int>{
        var s = seatBookStatus[startTime-8].union(seatBookStatus[startTime-7]).toMutableSet()
        for (index in (startTime-7) until (endTime-8)) {
            s = s.union(seatBookStatus[index]).toMutableSet()
        }
        return allSeats.subtract(s)
    }

    // 寻找相邻空座
    fun findAdjacent(seatID:Int, startTime: Int, endTime: Int): Int {
        val adjacentSeatID:Int = if (seatID % 2 == 1) seatID + 1 else seatID - 1
        return if (checkSeatStatusWhenBook(adjacentSeatID, startTime, endTime)) adjacentSeatID else 0
    }

    //寻找所有相邻空座
    fun findAlladjacent(seatIDList: MutableList<Int>, startTime: Int, endTime: Int): MutableList<Int> {
        var allAdjacentList = mutableListOf<Int>()
        for (seatID in seatIDList) {
            if (findAdjacent(seatID, startTime, endTime) != 0) allAdjacentList.add(findAdjacent(seatID, startTime, endTime))
        }
        return allAdjacentList
    }


}