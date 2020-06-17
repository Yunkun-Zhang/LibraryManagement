package com.example.librarymanagement.model

data class Seat(
    val free: Boolean,
    val seatID: Int,
    val status: String,
    val todayend1: Int,
    val todayend2: Int,
    val todaystart1: Int,
    val todaystart2: Int,
    val tomorrowend1: Int,
    val tomorrowend2: Int,
    val tomorrowstart1: Int,
    val tomorrowstart2: Int,
    val wait: Boolean
)