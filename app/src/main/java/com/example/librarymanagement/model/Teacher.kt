package com.example.librarymanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Teacher")
data class Teacher(

    @PrimaryKey(autoGenerate = true)
    var teacherID: Int?,
    @ColumnInfo(name = "t_name")
    var teacherName: String?,
    //教学年限
    @ColumnInfo(name = "t_year")
    var teachYear: Int?

)
