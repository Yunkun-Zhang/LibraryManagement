package com.example.librarymanagement.ui.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.R
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.StringCallback
import kotlinx.android.synthetic.main.activity_signup.*


class Signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        book_back.setOnClickListener {
            finish()
        }

        signup_submit.setOnClickListener {
            if (username.text.toString() != "" && password.text.toString() != "") {
                if (password.text.toString() != confirm_password.text.toString()) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setMessage("确认密码与密码不符！")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }
                else {
                    Okkt.instance.Builder().setUrl("/user/add").putBody(hashMapOf("name" to username.text.toString(),"password" to password.text.toString())).
                    postString(object: StringCallback {
                        override suspend fun onFailed(error: String) {
                            val alertDialog = AlertDialog.Builder(this@Signup)
                            alertDialog.setTitle("注册失败")
                            alertDialog.setMessage("请检查网络！")
                            alertDialog.setNeutralButton("确定", null)
                            alertDialog.show()
                        }

                        override suspend fun onSuccess(entity: String, flag: String) {
                            //添加好友表
                            Okkt.instance.Builder().setUrl("/friend/add").putBody(hashMapOf("userID" to entity)).
                                    postString(object:StringCallback{
                                        override suspend fun onFailed(error: String){}
                                        override suspend fun onSuccess(entity: String, flag: String){}
                                    })
                            // 注册完进入登录页面（addFlags防止回退）
                            val sign = Intent(this@Signup, Login::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            sign.putExtra("username", username.text.toString())
                            sign.putExtra("password", password.text.toString())
                            startActivity(sign)
                        }
                    })

                }
            }
            else {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("用户名或密码不应为空！")
                alertDialog.setNeutralButton("确定", null)
                alertDialog.show()
            }
        }
    }
}