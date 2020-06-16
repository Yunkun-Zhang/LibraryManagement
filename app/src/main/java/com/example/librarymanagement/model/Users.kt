package com.example.librarymanagement.model

data class Users(
    val userID: Int,
    val name: String,
    val password: String,
    val phone:String?,
    val email:String?,
    val gender:Boolean?,
    val token:String
)