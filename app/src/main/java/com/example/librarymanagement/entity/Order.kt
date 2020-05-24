package com.example.librarymanagement.entity


class Order(var OrderID:Int) {
    var userID: Int = 0
    var orderTime: Int = 0
    var beginTime: Int = 0
    var endTime: Int = 0
    var orderStatus: Boolean = false //finished or not
    var pairStatus:Boolean = false // pair order or not
    var subject:String = "" //temporarily string, maybe rewrite into enum later
    var gender:Boolean = true //true for male
}