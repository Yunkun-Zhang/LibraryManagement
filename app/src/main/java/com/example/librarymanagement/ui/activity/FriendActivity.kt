package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.FriendList
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_friend.*
import org.jetbrains.anko.*

class FriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
        val userID = intent.getIntExtra("userID", 0)

        // 获取好友列表
        val friends = arrayListOf<Int>()
        var recyclerView: RecyclerView = find(R.id.friend_list)
        var list = arrayListOf<String>()
        var imagelist = arrayListOf<Int>()
        for (i in 1..20) {
            list.add("这是第${i}个好友")
            imagelist.add(R.drawable.headportrait)
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        // layoutManager
        recyclerView.layoutManager = layoutManager
        // setAdapter
        val adapter = FriendList(this, list, imagelist)
        friend_list.adapter = adapter
        // itemClick
        val personInfoPage = Intent(this, PersonInfoActivity::class.java)
        adapter!!.setOnKotlinItemClickListener(object : FriendList.IKotlinItemClickListener {
            override fun onItemClickListener(position: Int) {
                // toast("点击了$position")
                personInfoPage.apply {
                    putExtra("friend", true)
                    putExtra("userID", position+1)  // 传入好友的userID
                    startActivity(this)
                }
            }
        })

        // 返回按钮
        btn_back.setOnClickListener { finish() }

        // 在消息页面和好友页面切换，其中消息页面每次都会刷新
        friend.setOnClickListener {
            tv_title.text = "好友"
            friend_list.visibility = View.VISIBLE
            container.visibility = View.GONE
        }
        text.setOnClickListener {
            if (tv_title.text == "好友") {
                tv_title.text = "消息"
                friend_list.visibility = View.GONE
                container.visibility = View.VISIBLE
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

    }
}
