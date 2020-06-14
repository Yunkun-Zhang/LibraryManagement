package com.example.librarymanagement.model

data class Users(
    val name: String,
    val password: String,
    val userID: Int
)

data class UsersList(
    val users: MutableList<Users>
)