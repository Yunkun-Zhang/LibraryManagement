package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.control.IMcontroler
import com.example.librarymanagement.model.Users
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        book_back.setOnClickListener {
            finish()
        }

        if(intent.hasExtra("username")&&intent.hasExtra("password")) {
            //注册过来直接登录
            val name_signup = intent.getStringExtra("username")
            val password_signup = intent.getStringExtra("password")

            if (name_signup != "" && password_signup != "") {
                Okkt.instance.Builder().setUrl("/user/findbyname").putBody(hashMapOf("name" to name_signup))
                    .post(object : CallbackRule<Users> {
                        override suspend fun onFailed(error: String) {
                            val alertDialog = AlertDialog.Builder(this@Login)
                            alertDialog.setTitle("登陆失败")
                            alertDialog.setMessage("请检查网络")
                            alertDialog.setNeutralButton("确定", null)
                            alertDialog.show()
                        }

                        override suspend fun onSuccess(entity: Users, flag: String) {
                            if (password_signup == entity.password) {
                                //连接融云 taken应该从数据库中获得
                                IMcontroler().connect(entity.token)
                                val intent = Intent(
                                    this@Login,
                                    MainActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.putExtra("userID", entity.userID)
                                startActivity(intent)


                            } else {
                                val alertDialog = AlertDialog.Builder(this@Login)
                                alertDialog.setTitle("登陆失败")
                                alertDialog.setMessage("用户名或密码错误！")
                                alertDialog.setNeutralButton("确定", null)
                                alertDialog.show()
                            }
                        }
                    })
            }
        }


        login_submit.setOnClickListener {
            val name = username.text.toString()
            val password = password.text.toString()

            Okkt.instance.Builder().setUrl("/user/findbyname").putBody(hashMapOf("name" to name)).
            post(object:CallbackRule<Users> {
                override suspend fun onFailed(error: String) {
                    val alertDialog = AlertDialog.Builder(this@Login)
                    alertDialog.setTitle("登陆失败")
                    alertDialog.setMessage("请检查网络")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }

                override suspend fun onSuccess(entity: Users, flag: String) {
                    if(password == entity.password){
                        val intent = Intent(this@Login, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("userID", entity.userID)
                        intent.putExtra("username",entity.name)
                        startActivity(intent)

                        //连接融云 taken应该从数据库中获得
                        IMcontroler().connect(entity.token)
                    }
                    else{
                        val alertDialog = AlertDialog.Builder(this@Login)
                        alertDialog.setTitle("登陆失败")
                        alertDialog.setMessage("用户名或密码错误！")
                        alertDialog.setNeutralButton("确定", null)
                        alertDialog.show()
                    }
                }
            })
        }

        login_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply{
                startActivity(this)
            }
        }
    }
}