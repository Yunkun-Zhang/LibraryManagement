package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userID: Int?,
    @ColumnInfo(name = "nickname")
    var nickname: String?,
    @ColumnInfo(name = "pwd")
    var password: String?,
    @ColumnInfo(name = "phnum")
    var phonenumber: String?,
    @ColumnInfo(name = "email")
    var email: String?,
    @ColumnInfo(name = "gender")
    var gender: Boolean  // true for male, false for female
)