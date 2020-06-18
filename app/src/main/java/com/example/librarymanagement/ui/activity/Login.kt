package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.control.IMcontroler
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_login.*
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec


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

            val publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiEpz2w6VaIAkuqO6p2pGUF7VndsbhrBXeohMaFbVngIs+gSpWUZtMfhTTogkZNwxv3FRy0NophPOBY+LI0dBH8wKpNsqeLhqUkEVQeQGbL1xzxKKfoO1H0NnYrSsaYGNzcAMMoifFi0hdTOw7qT0WpO61EFWy3ZzUSKI4PLFVtNmcqwWgkL1lwtoo9MhhSZItsQe9HGzv8FR0Amh0epfEJq+XVWUAnvbzj3w60nYW8cTMVdowOrYQBLMH2ZQoZ+KNLEJvmGe44wAgRk6+V8UZpxFG2Emj6diJohcG7ajv+7EN5Nh2ym6QhqVNA6SJBagVJmsgUXmRs3kQkqevaqpYQIDAQAB"
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr,Base64.DEFAULT)))
            val encryptedPassword = RSACrypt.encryptByPublicKey(password_signup,publicKey)

            if (name_signup != "" && password_signup != "") {
                Okkt.instance.Builder().setUrl("/user/verify").putBody(hashMapOf("name" to name_signup,"password" to encryptedPassword))
                    .post(object : CallbackRule<User> {
                        override suspend fun onFailed(error: String) {
                            val alertDialog = AlertDialog.Builder(this@Login)
                            alertDialog.setTitle("登录失败")
                            alertDialog.setMessage("请检查网络")
                            alertDialog.setNeutralButton("确定", null)
                            alertDialog.show()
                        }

                        override suspend fun onSuccess(entity: User, flag: String) {
                            if (entity.userID != -1) {
                                //连接融云 taken应该从数据库中获得
                                IMcontroler().connect(entity.token)
                                val intent = Intent(
                                    this@Login,
                                    MainActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.putExtra("userID", entity.userID)
                                intent.putExtra("name",entity.name)
                                startActivity(intent)
                            } else {
                                val alertDialog = AlertDialog.Builder(this@Login)
                                alertDialog.setTitle("登录失败")
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

            val publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiEpz2w6VaIAkuqO6p2pGUF7VndsbhrBXeohMaFbVngIs+gSpWUZtMfhTTogkZNwxv3FRy0NophPOBY+LI0dBH8wKpNsqeLhqUkEVQeQGbL1xzxKKfoO1H0NnYrSsaYGNzcAMMoifFi0hdTOw7qT0WpO61EFWy3ZzUSKI4PLFVtNmcqwWgkL1lwtoo9MhhSZItsQe9HGzv8FR0Amh0epfEJq+XVWUAnvbzj3w60nYW8cTMVdowOrYQBLMH2ZQoZ+KNLEJvmGe44wAgRk6+V8UZpxFG2Emj6diJohcG7ajv+7EN5Nh2ym6QhqVNA6SJBagVJmsgUXmRs3kQkqevaqpYQIDAQAB"
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr,Base64.DEFAULT)))
            val encryptedPassword = RSACrypt.encryptByPublicKey(password,publicKey)

            Okkt.instance.Builder().setUrl("/user/verify").putBody(hashMapOf("name" to name,"password" to encryptedPassword)).
            post(object:CallbackRule<User> {
                override suspend fun onFailed(error: String) {
                    val alertDialog = AlertDialog.Builder(this@Login)
                    alertDialog.setTitle("登录失败")
                    alertDialog.setMessage("请检查网络或者联系管理员")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }

                override suspend fun onSuccess(entity: User, flag: String) {
                    if(entity.userID != -1){
                        //连接融云 taken应该从数据库中获得
                        IMcontroler().connect(entity.token)
                        val intent = Intent(this@Login, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("userID", entity.userID)
                        intent.putExtra("name",entity.name)
                        startActivity(intent)
                    }
                    else{
                        val alertDialog = AlertDialog.Builder(this@Login)
                        alertDialog.setTitle("登录失败")
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