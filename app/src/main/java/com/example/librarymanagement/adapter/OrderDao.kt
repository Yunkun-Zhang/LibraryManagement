package com.example.librarymanagement.adapter

import androidx.room.*
import com.example.librarymanagement.model.Order

@Dao
interface OrderDao:BaseDao<Order> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: Order)

    @Query("select * from `Order`")
    fun getAllOrders():MutableList<Order>

    @Query("select * from `Order` where orderID = :orderID")
    fun getOrder(orderID:Int): Order

    @Query("select seat_id from `Order` where gender = :male")
    fun getOrderByGender(male:Boolean): MutableList<Int>

    @Query("select seat_id from `Order` where NOT (begin_time <= :beginTime AND end_time <= :beginTime OR end_time >= :endTime AND begin_time >= :endTime)")
    fun getOrderByTimePeriod(beginTime:Int, endTime:Int): MutableList<Int>

    @Query("select seat_id from `Order` where subject = :subject ")
    fun getOrderBySubject(subject: String): MutableList<Int>

    @Query("select seat_id from `Order` where subject = :subject AND NOT (begin_time <= :beginTime AND end_time <= :beginTime OR end_time >= :endTime AND begin_time >= :endTime)")
    fun getOrderBySubjectAndTimePeriod(subject: String, beginTime: Int, endTime: Int): MutableList<Int>

    @Query("select seat_id from `Order` where gender = :male AND NOT (begin_time <= :beginTime AND end_time <= :beginTime OR end_time >= :endTime AND begin_time >= :endTime)")
    fun getOrderByGenderAndTimePeriod(male: Boolean, beginTime: Int, endTime: Int): MutableList<Int>

    @Query("select seat_id from `Order` where subject = :subject AND NOT (begin_time <= :beginTime AND end_time <= :beginTime OR end_time >= :endTime AND begin_time >= :endTime) AND gender = :male")
    fun getOrderBySubjectAndTimePeriodAndGender(subject: String, beginTime: Int, endTime: Int, male:Boolean): MutableList<Int>

    @Query("delete from `Order`")
    fun deleteAll()

    @Query("select IFNULL(MAX(orderID),0) from `Order`")
    fun getMAXOrderID():Int

}
