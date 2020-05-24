package com.example.librarymanagement.adapter

import androidx.room.*
import com.example.librarymanagement.model.Student

@Dao
interface StudentDao:BaseDao<Student> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: Student)

    @Query("select * from Student")
    fun getAllStudents():MutableList<Student>

    @Query("select * from Student where studentID = :studentID")
    fun getStudent(studentID:Int): Student

    @Query("select * from Student order by studentID desc ")
    fun getAllByDateDesc():MutableList<Student>

    @Query("delete from Student")
    fun deleteAll()

    @Query("select * from Student where studentID > :studentID")
    fun getStudentidg(studentID:Int):MutableList<Student>

}
