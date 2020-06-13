package com.example.librarymanagement.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.librarymanagement.R
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imlib.model.Conversation

internal class ConversationListActivity : FragmentActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)
        val conversationListFragment = ConversationListFragment()

        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示 
        val uri = Uri.parse("rong://" + applicationInfo.packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(
                Conversation.ConversationType.PRIVATE.getName(),
                "false"
            ) //设置私聊会话非聚合显示
            .appendQueryParameter(
                Conversation.ConversationType.GROUP.getName(),
                "true"
            ) //设置群组会话聚合显示
            .appendQueryParameter(
                Conversation.ConversationType.DISCUSSION.getName(),
                "false"
            ) //设置讨论组会话非聚合显示
            .appendQueryParameter(
                Conversation.ConversationType.SYSTEM.getName(),
                "false"
            ) //设置系统会话非聚合显示
            .build()
        conversationListFragment.setUri(uri)
        val manager: FragmentManager = getSupportFragmentManager()
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationListFragment)
        transaction.commit()

    }
}