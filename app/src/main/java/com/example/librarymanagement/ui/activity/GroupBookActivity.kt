package com.example.librarymanagement.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.R
import kotlinx.android.synthetic.main.activity_group_book.*

class GroupBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_book)

        btn_back.setOnClickListener { finish() }
    }
}
