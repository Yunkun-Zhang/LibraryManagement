package com.example.librarymanagement.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

//所有activity的基类
abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initListener()
        initData()
    }

    protected fun initData() {

    }

    protected fun initListener() {

    }

    // 获取布局id
    abstract fun getLayoutId(): Int


}