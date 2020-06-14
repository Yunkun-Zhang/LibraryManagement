package com.example.librarymanagement.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.librarymanagement.R
import com.example.librarymanagement.control.setUserInfo
import io.rong.imkit.fragment.ConversationFragment

internal class ConversationActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_conversation)

        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val manager: FragmentManager = getSupportFragmentManager()
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()

        setUserInfo().setUserInfo()
    }
}