package com.example.librarymanagement.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_person_info.*

class PersonInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        btn_back.setOnClickListener { finish() }
    }
}
