package com.example.librarymanagement.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.FriendList
import com.example.librarymanagement.model.Friend
import com.example.librarymanagement.model.Invitation
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_friend.*
import org.jetbrains.anko.find


class FriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
        val userID = intent.getIntExtra("userID", 0)
        val name = intent.getStringExtra("name")

        // 返回按钮
        btn_back.setOnClickListener { finish() }

        // 每次进入好友页面，判断有没有好友申请
        Okkt.instance.Builder().setUrl("/invite/getInvitation").putBody(hashMapOf("receiveID" to userID.toString())).
        post(object: CallbackRule<MutableList<Invitation>> {
            override suspend fun onFailed(error: String){
            }
            override suspend fun onSuccess(entity: MutableList<Invitation>, flag: String) {
                for(i in entity){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@FriendActivity)
                    builder.setTitle("好友申请")
                    builder.setMessage("申请人：${i.sendname}")
                    builder.setPositiveButton("拒绝") { _, _ ->}
                    builder.setNegativeButton("接受") { _, _ ->
                        Okkt.instance.Builder().setUrl("/friend/addfriend").setParams(hashMapOf("userID" to i.sendID.toString(), "name" to name)).
                        post(object: CallbackRule<MutableList<Invitation>> {
                            override suspend fun onFailed(error: String){}
                            override suspend fun onSuccess(entity: MutableList<Invitation>, flag: String) {}})
                        Okkt.instance.Builder().setUrl("/friend/addfriend").setParams(hashMapOf("userID" to i.receiveID.toString(), "name" to i.sendname)).
                        post(object: CallbackRule<MutableList<Invitation>> {
                            override suspend fun onFailed(error: String){}
                            override suspend fun onSuccess(entity: MutableList<Invitation>, flag: String) {}})
                    }
                    builder.show()
                }
            }
        })

        // 获取好友列表
        Okkt.instance.Builder().setUrl("/friend/getallfriendnames").setParams(hashMapOf("userID" to userID.toString()))
            .get(object: CallbackRule<Friend> {
            override suspend fun onFailed(error: String){
                val alertDialog = AlertDialog.Builder(this@FriendActivity)
                alertDialog.setTitle("获取好友列表失败")
                alertDialog.setMessage(error)
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
            override suspend fun onSuccess(entity: Friend, flag: String) {
                // 处理recyclerView，显示好友列表
                val friends: MutableList<String> = entity.friend_names.split(',').toMutableList()
                friends.removeAt(0)
                val recyclerView: RecyclerView = find(R.id.friend_list)
                val list = arrayListOf<String>()
                val imagelist = arrayListOf<Int>()
                for (i in friends) {
                    list.add(i)
                    imagelist.add(R.drawable.headportrait)
                }
                val layoutManager = LinearLayoutManager(this@FriendActivity)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                // layoutManager
                recyclerView.layoutManager = layoutManager
                // setAdapter
                val adapter = FriendList(this@FriendActivity, list, imagelist)
                friend_list.adapter = adapter
                // itemClick
                adapter.setOnKotlinItemClickListener(object : FriendList.IKotlinItemClickListener {
                    override fun onItemClickListener(position: Int) {
                        // 将好友名字传入个人信息页
                        val personInfoPage = Intent(this@FriendActivity, PersonInfoActivity::class.java)
                        personInfoPage.apply {
                            putExtra("friendname", friends[position])
                            putExtra("friendid", 0)
                            startActivity(this)
                        }
                    }
                })

                // 在消息页面和好友页面切换，其中消息页面每次都会刷新
                friend.setOnClickListener {
                    tv_title.text = "好友"
                    friend_list.visibility = View.VISIBLE
                    container.visibility = View.GONE
                }
                text.setOnClickListener {
                    if (tv_title.text == "好友") {
                        // 隐藏好友列表，显示对话列表
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
        })

    }
}
