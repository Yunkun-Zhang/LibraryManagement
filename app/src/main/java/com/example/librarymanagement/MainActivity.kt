package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.ui.activity.RoomTest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_test.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        index.setOnClickListener {
            //val intent = Intent(this, RoomTest::class.java)
            //startActivity(intent)
            Intent(this, RoomTest::class.java).apply { startActivity(this) }
        }
    }
}

