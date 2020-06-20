package com.example.librarymanagement.ui.activity


import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.librarymanagement.R
import com.example.librarymanagement.control.SetUserInfo
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import io.rong.imkit.fragment.ConversationFragment
import kotlinx.android.synthetic.main.activity_conversation.*


internal class ConversationActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_conversation)

        // 返回
        btn_back.setOnClickListener { finish() }

        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val manager: FragmentManager = getSupportFragmentManager()
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()

        val targetId:String = getIntent().getData().getQueryParameter("targetId")
        Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userid" to targetId))
            .post(object : CallbackRule<User> {
                override suspend fun onFailed(error: String) {
                }
                override suspend fun onSuccess(entity: User, flag: String) {
                    SetUserInfo().setUserInfo(entity.name)
                    name.text = entity.name
                }
            })
    }
}
