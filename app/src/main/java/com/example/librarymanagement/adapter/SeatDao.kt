package com.example.librarymanagement.adapter

import androidx.room.*
import com.example.librarymanagement.model.Seat

@Dao
interface SeatDao:BaseDao<Seat> {

    @Insert
    fun insert(element: Seat)

    @Query("select * from Seat")
    fun getAllSeats():MutableList<Seat>

    @Query("select * from Seat where SeatID = :SeatID")
    fun getSeatID(SeatID:Int): Seat

    @Query("delete from Seat")
    fun deleteAll()

}
