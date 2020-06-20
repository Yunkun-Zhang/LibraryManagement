package com.example.librarymanagement.model

import com.example.librarymanagement.others.UserStatus

data class User(
    val userID: Int,
    val name: String,
    val password: String,
    val phone:String?,
    val email:String?,
    val gender:Boolean?,
    val token:String,
    val favorsubject:String?,
    val status:UserStatus,
    val reserved:Boolean
)