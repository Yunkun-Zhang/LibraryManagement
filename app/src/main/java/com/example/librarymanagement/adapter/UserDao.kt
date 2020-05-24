package com.example.librarymanagement.adapter

import androidx.room.*
import com.example.librarymanagement.model.User

@Dao
interface UserDao:BaseDao<User> {

    @Insert
    fun insert(element: User)

    @Query("select * from User")
    fun getAllUsers():MutableList<User>

    @Query("select * from User where userID = :userID")
    fun getUserID(userID:Int): User

    @Query("select * from User where gender = :male ")
    fun getByGender(male: Boolean):MutableList<User> // true for male, false for female

    @Query("delete from User")
    fun deleteAll()

}
