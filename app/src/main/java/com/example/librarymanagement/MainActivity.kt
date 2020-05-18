package com.example.librarymanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.model.Student


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("you are here")
    }



}

