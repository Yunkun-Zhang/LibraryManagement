package com.example.librarymanagement.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_book.*
import org.jetbrains.anko.sdk25.coroutines.onTimeChanged


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        time.setIs24HourView(true)
        time.setOnTimeChangedListener { TimePicker, i, i2 ->

        }

        book_back.setOnClickListener {
            finish()
        }
        /*
        login_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply{
                startActivity(this)
            }
        }
        */
    }

}