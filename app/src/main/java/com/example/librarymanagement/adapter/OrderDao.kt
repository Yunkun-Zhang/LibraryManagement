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

    @Query("select * from `Order` order by orderID desc ")
    fun getAllByDateDesc():MutableList<Order>

    @Query("delete from `Order`")
    fun deleteAll()

}
