package com.example.librarymanagement.control

import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.entity.Seat
import com.example.librarymanagement.extension.DateUtil
import com.example.librarymanagement.model.Order

class OrderControl {
    val seatcontrol = SeatControl()

    fun pair(subject:String, starttime:Int, endtime:Int): Array<Int> {
        val oBao = AppDataBase.instance.getOrderDao()
        val orders = oBao.getOrderBySubject(subject)
        for(order in orders){
            val free_seat_around = seatcontrol.findAdjacent(order.seatID,order.beginTime,order.endTime)
            if (free_seat_around != 0 && ((starttime >= order.beginTime) or (endtime <= order.endTime))){
                return arrayOf(free_seat_around, order.userID)
            }
        }
        return arrayOf(0,0)
    }

    fun reserve(order:Order){
        seatcontrol.setSeatBooked(order.seatID, order.beginTime,order.endTime)
        val oBao = AppDataBase.instance.getOrderDao()
        oBao.insert(order)
    }

    fun creat_order(userID:Int, seatID:Int, beginTime:Int, endTime:Int, pairStatus:Boolean, subject:String, gender:Boolean):Order{
        val time = DateUtil.nowDateTime
        val oBao = AppDataBase.instance.getOrderDao()
        val orderID = oBao.getMAXOrderID()
        val new_order=Order(orderID+1,userID,seatID,time,beginTime,endTime,false,pairStatus,subject, gender)
        return new_order
    }
}