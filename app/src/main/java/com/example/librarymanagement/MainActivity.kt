package com.example.librarymanagement

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.model.Users
import com.example.librarymanagement.others.*
import com.example.librarymanagement.ui.activity.*
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    //扫码需要参数
    private val REQUEST_CODE_SCAN = 0X01
    val DEFAULT_VIEW = 0x22

    // 必要变量：userID
    var userID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置

        // 获取userID
        userID = intent.getIntExtra("userID", 0)
        // 获取user状态
        var userStatus: UserStatus = UserStatus.FREE
        if (userID != 0) {
            Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userId" to userID.toString())).
            post(object: CallbackRule<Users> {
                override suspend fun onFailed(error: String) {
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("个人信息获取失败")
                    alertDialog.setMessage("请检查网络")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }

                override suspend fun onSuccess(entity: Users, flag: String) {
                    //测试使用,实现时可以删掉
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("个人信息获取成功")
                    alertDialog.setMessage("Congratulations!!!")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()

                    //当前用户信息只能获取这些，后续需要等待服务器修改
                    val name = entity.name
                    val email = entity.email
                    val gender = entity.gender
                    val phone = entity.phone
                    val favorsubject = entity.favorsubject
                }
            })
        }

        // 从预订界面返回
        var seat = intent.getIntExtra("seat", 0)
        if (seat != 0) {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("预订 $seat 座位成功！")
            alertDialog.setNeutralButton("确定", null)
            alertDialog.show()
        }
        var orderID = intent.getIntExtra("orderID", 0)

        // 界面的一些显示更改
        if (userID != 0) {
            if (userStatus == UserStatus.FREE) login_state.text = "空闲"
            else if (userStatus == UserStatus.ACTIVE) login_state.text = "占座中"
            else if (userStatus == UserStatus.LEAVE) login_state.text = "暂离"
            log_or_sign.visibility = View.GONE
            logout.visibility = View.VISIBLE
        }
        else {
            login_state.text = "未登录"
            log_or_sign.visibility = View.VISIBLE
            logout.visibility = View.GONE
        }

        // 扫码
        main_to_scan.setOnClickListener {
            if (userStatus == UserStatus.FREE) { newViewBtnClick(main_to_scan) }
        }

        // 去登录页面
        main_to_login.setOnClickListener {
            Intent(this, Login::class.java).apply {
                startActivity(this)
            }
        }
        // 去注册页面
        main_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply {
                startActivity(this)
            }
        }

        main_to_book.setOnClickListener {
            // 先判断是否登录
            if (userID == 0) {
                toast("请先登录！")
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else {
                Intent(this, Book::class.java).apply {
                    putExtra("userID", userID)
                    putExtra("orderID", orderID)
                    startActivity(this)
                }
            }
        }
        // 去好友页面，该页面应当包括好友列表和对话列表
        main_to_friends.setOnClickListener {
            // 先判断是否登录
            if (userID == 0) {
                toast("请先登录！")
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else {
                Intent(this, FriendActivity::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }
        // 去个人信息页面
        main_to_personal.setOnClickListener {
            // 先判断是否登录
            if (userID == 0) {
                toast("请先登录！")
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else {
                Intent(this, PersonInfoActivity::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }
        // 去查看座位页面
        main_to_check.setOnClickListener {
            Intent(this, SeatInfoActivity::class.java).apply {
                putExtra("check", true)
                val form = SimpleDateFormat("HH")
                val hour = form.format(Date()).toInt()
                putExtra("hour", hour)
                startActivity(this)
            }
        }
        // 登出
        logout.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }


    }

    /**
     * 扫码实现开始
     */
    fun newViewBtnClick(view: View?) {
        //DEFAULT_VIEW为用户自定义用于接收权限校验结果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                DEFAULT_VIEW
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (requestCode == DEFAULT_VIEW) {
            //调用DefaultView扫码界面
            ScanUtil.startScan(
                this@MainActivity,
                REQUEST_CODE_SCAN,
                HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //当扫码页面结束后，处理扫码结果
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        if (requestCode == REQUEST_CODE_SCAN) {
            val obj: Any = data.getParcelableExtra(ScanUtil.RESULT)
            if (obj is HmsScan) {
                if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                    //判断是否登录
                    if (login_state.text.toString() == "未登录") {
                        toast("请先登录！")
                        Intent(this, Login::class.java).apply {
                            startActivity(this)
                        }
                    }
                    else if (false) { // 当前用户是否正在占座

                    }
                    else{
                        val seatID = obj.getOriginalValue().toInt()
                        // 先判断扫到的座位是否为当前用户的预订，若是，则更新座位状态
                        val ordered_seat = 1011
                        if (seatID == ordered_seat) {
                            // 更新座位状态
                        }
                        else {  // 若否，判断扫到座位是否可用，若是，则成功占位
                            val app = MyApplication.instance()
                            val seatControl = app.seatControl
                            val form = SimpleDateFormat("HH")
                            val hour = form.format(Date()).toInt()
                            val seat_list = seatControl.querySeatByTime(hour, hour+1)
                            if (seatID in seat_list) {
                                val alertDialog = AlertDialog.Builder(this)
                                alertDialog.setMessage("成功占座")
                                alertDialog.setNeutralButton("确定", null)
                                alertDialog.show()
                                // 更改user状态
                                Intent(this, MainActivity::class.java).apply {
                                    putExtra("userID", userID)
                                    putExtra("seatID", seatID)
                                    // user状态
                                    startActivity(this)
                                }
                            }
                            else {
                                alert("该座位已被占用！") { positiveButton("确定") {} }
                            }

                        }

                        //obj.getOriginalValue()就是扫码出来的信息，类型为String
                        //如果未占位，查询是否被预定，没预定就占位
                        //如果已经占了该位置，就改为leave
                        //这行代码是扫码后的提示框，可以考虑删掉

                        Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
                        //这个是在logcat显示相关信息的，可以删掉
                        Log.i("resultscan",obj.getOriginalValue())
                    }
                }
                return
            }
        }

    }
    /**扫码实现结束*/

}

