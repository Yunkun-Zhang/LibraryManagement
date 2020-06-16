package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_mod_info.*
import kotlinx.android.synthetic.main.activity_mod_info.gender

class ModInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_info)

        val userID = intent.getIntExtra("userID", 0)
        // 获取userID对应用户的信息，修改显示
        Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userid" to userID.toString())).
        post(object: CallbackRule<User> {
            override suspend fun onFailed(error: String) {
                val alertDialog = AlertDialog.Builder(this@ModInfoActivity)
                alertDialog.setTitle("个人信息获取失败")
                alertDialog.setMessage("请检查网络")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }

            override suspend fun onSuccess(entity: User, flag: String) {
                //显示这些信息
                pwd.text = Editable.Factory.getInstance().newEditable(entity.password)

                if (entity.gender == true) gender.setSelection(1)
                else if (entity.gender == false) gender.setSelection(2)
                else gender.setSelection(0)

                phone.text = Editable.Factory.getInstance().newEditable(entity.phone)

                email.text = Editable.Factory.getInstance().newEditable(entity.email)

                favorsubject.text = Editable.Factory.getInstance().newEditable(entity.favorsubject)
            }
        })

        btn_back.setOnClickListener { finish() }

        // 保存
        save.setOnClickListener {
            // 保存
            Okkt.instance.Builder().setUrl("/user/revisepasswordbyid").setParams(hashMapOf("userid" to userID.toString()))
                .putBody(hashMapOf("password" to pwd.text.toString()))
                .post(object: CallbackRule<String> {
                    override suspend fun onFailed(error: String) { }
                    override suspend fun onSuccess(entity: String, flag: String) { }
                })
            val g: String
            if (gender.selectedItem == "男") g = "true"
            else g = "false"

            // 改密码（可做额外接口）
            Okkt.instance.Builder().setUrl("/user/revisepasswordbyid")
                .setParams(hashMapOf("userid" to userID.toString()))
                .putBody(hashMapOf("password" to pwd.text.toString()))
                .post(object: CallbackRule<String> {
                    override suspend fun onFailed(error: String) { }
                    override suspend fun onSuccess(entity: String, flag: String) { }
                })
            // 其他信息
            Okkt.instance.Builder().setUrl("/user/reviseinfobyid")
                .setParams(hashMapOf("userid" to userID.toString()))
                .putBody(hashMapOf("gender" to g,
                    "phone" to phone.text.toString(),
                    "favorsubject" to favorsubject.text.toString(),
                    "email" to email.text.toString()))
                .post(object: CallbackRule<String> {
                    override suspend fun onFailed(error: String) { }
                    override suspend fun onSuccess(entity: String, flag: String) { }
                })

            var intent = Intent()
            intent.putExtra("userID", userID)
            finish()
        }

    }
}