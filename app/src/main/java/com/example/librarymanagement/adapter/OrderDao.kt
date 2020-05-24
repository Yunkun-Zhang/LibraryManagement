package com.example.librarymanagement.adapter

import androidx.room.*
import com.example.librarymanagement.model.Order
import javax.security.auth.Subject

@Dao
interface OrderDao:BaseDao<Order> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: Order)

    @Query("select * from `Order`")
    fun getAllOrders():MutableList<Order>

    @Query("select * from `Order` where orderID = :orderID")
    fun getOrder(orderID:Int): Order

    @Query("select * from `Order` where gender = :male")
    fun getOrderByGender(male:Boolean): MutableList<Order>

    @Query("select * from `Order` where NOT (begin_time <= :beginTime AND end_time <= :beginTime OR end_time >= :endTime AND begin_time >= :endTime)")
    fun getOrderByTimePeriod(beginTime:Int, endTime:Int): MutableList<Order>

    @Query("select * from `Order` where subject = :subject ")
    fun getOrderBySubject(subject: String): MutableList<Order>

    @Query("delete from `Order`")
    fun deleteAll()

}
