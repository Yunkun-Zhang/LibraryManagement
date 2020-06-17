package com.example.librarymanagement.model

data class Reservation(
    val companion: Int?,
    val endtime: Int,
    val hang: Boolean,
    val ordertime: String,
    val pairstatus: Boolean,
    val reservationid: Int,
    val seatID: Int,
    val selfgender: Boolean?,
    val starttime: Int,
    val subject: String?,
    val targetgender: Boolean?,
    val userID: Int
)