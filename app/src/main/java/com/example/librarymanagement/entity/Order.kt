package com.example.librarymanagement.entity


class Order(var OrderID:Int) {
    var userID: Int = 0
    var ordertime: Int = 0
    var begintime: Int = 0
    var endtime: Int = 0
    var orderstatus: Boolean = false //finished or not
    var pairstatus:Boolean = false // pair order or not
    var subject:String = "" //temporarily string, maybe rewrite into enum later
    var gender:Boolean = true //true for male
}