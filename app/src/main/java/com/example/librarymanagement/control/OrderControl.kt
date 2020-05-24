package com.example.librarymanagement.control

import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.extension.DateUtil
import com.example.librarymanagement.model.Order

class OrderControl {
    val oBao = AppDataBase.instance.getOrderDao()

    //寻找配对对象和位置
    fun findPairedSeats(subject:String?, male: Boolean?, startTime:Int, endTime:Int): MutableList<Int>? {

        return if (subject == null) {
            if (male == null) {
                oBao.getOrderByTimePeriod(startTime, endTime)
            } else {
                oBao.getOrderByGenderAndTimePeriod(male, startTime, endTime)
            }
        } else if (male == null) {
            oBao.getOrderBySubjectAndTimePeriod(subject, startTime, endTime)
        } else oBao.getOrderBySubjectAndTimePeriodAndGender(subject,startTime,endTime,male)
    }

    //确认订单，添加到库中
    fun confirmOrder(order:Order){
        oBao.insert(order)
    }

    //订单的创建
    fun creat_order(userID:Int, seatID:Int, beginTime:Int, endTime:Int, pairStatus:Boolean, subject:String, gender:Boolean):Order{
        val time = DateUtil.nowDateTime
        val oBao = AppDataBase.instance.getOrderDao()
        val orderID = oBao.getMAXOrderID()
        val new_order=Order(orderID+1,userID,seatID,time,beginTime,endTime,false,pairStatus,subject, gender)
        return new_order
    }
}