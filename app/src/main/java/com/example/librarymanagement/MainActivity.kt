package com.example.librarymanagement

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.model.Seat
import com.example.librarymanagement.model.User
import com.example.librarymanagement.others.*
import com.example.librarymanagement.ui.activity.*
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mod_info.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    //扫码需要参数
    private val REQUEST_CODE_SCAN = 0X01
    val DEFAULT_VIEW = 0x22

    // 必要变量：userID
    var userID = 0
    // var userStatus: UserStatus = UserStatus.FREE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置

        // 获取userID
        userID = intent.getIntExtra("userID", 0)
        // 获取当前占座seatID
        val now_seat = intent.getIntExtra("now_seat", 0)
        val name = intent.getStringExtra("name")
        // 获取user状态
        if (userID != 0) {
            Okkt.instance.Builder().setUrl("/user/findbyuserid").putBody(hashMapOf("userid" to userID.toString())).
            post(object: CallbackRule<User> {
                override suspend fun onFailed(error: String) {
                    alert("请检查网络") {
                        setTitle("个人信息获取失败")
                        positiveButton("确定") {}
                    }.show()
                }
                override suspend fun onSuccess(entity: User, flag: String) {
                    //当前用户信息只能获取这些，后续需要等待服务器修改
                    val userStatus = entity.status
                    if (userStatus == UserStatus.FREE) login_state.text = "空闲"
                    else {
                        back_to_seat.visibility = View.VISIBLE
                        if (userStatus == UserStatus.ACTIVE) login_state.text = "占座中"
                        else login_state.text = "暂离"
                    }
                }
            })
        }

        // 获取座位状态
        Okkt.instance.Builder().setUrl("/seat/getspareseats/now")
            .post(object : CallbackRule<MutableList<Int>> {
                override suspend fun onFailed(error: String) {
                    alert("图书馆未开放！") { positiveButton("确定") {} }.show()
                    emptynum.text = ""
                }
                override suspend fun onSuccess(entity: MutableList<Int>, flag: String) {
                    emptynum.text = entity.size.toString()
                }
            })

        // 界面的一些显示更改
        if (userID != 0) {
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
            // 先判断是否登录
            if (userID == 0) {
                toast("请先登录！")
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else if (login_state.text == "空闲") { newViewBtnClick(main_to_scan) }
            else {
                alert("正在占座中！点击确定返回占座") { positiveButton("确定") {
                    // 返回，功能同back_to_seat按钮
                    val h = intent.getIntExtra("hour", 0)
                    val m = intent.getIntExtra("min", 0)
                    val s = intent.getIntExtra("second", 0)
                    Intent(this@MainActivity, StudyActivity::class.java).apply {
                        putExtra("userID", userID)
                        putExtra("now_seat", now_seat)
                        putExtra("hour", h)
                        putExtra("min", m)
                        putExtra("second", s)
                        startActivity(this)
                    }
                } }.show()
            }
        }
        //
        val h = intent.getIntExtra("hour", 0)
        val m = intent.getIntExtra("min", 0)
        val s = intent.getIntExtra("second", 0)
        back_to_seat.setOnClickListener {
            Intent(this, StudyActivity::class.java).apply {
                putExtra("userID", userID)
                putExtra("now_seat", now_seat)
                putExtra("hour", h)
                putExtra("min", m)
                putExtra("second", s)
                startActivity(this)
            }
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
                    putExtra("now_seat", now_seat)
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
                    putExtra("name",name)
                    putExtra("now_seat", now_seat)
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
                    putExtra("now_seat", now_seat)
                    startActivity(this)
                }
            }
        }
        // 去查看座位页面
        main_to_check.setOnClickListener {
            // 请求服务器找到可用的seat_list
            Okkt.instance.Builder().setUrl("/seat/getspareseats/now")
                .post(object : CallbackRule<MutableList<Int>> {
                    override suspend fun onFailed(error: String) {
                        alert("图书馆未开放！") { positiveButton("确定") {} }.show()
                    }
                    override suspend fun onSuccess(entity: MutableList<Int>, flag: String) {
                        emptynum.text = entity.size.toString()
                        Intent(this@MainActivity, SeatInfoActivity::class.java).apply {
                            putExtra("check", true)
                            val form = SimpleDateFormat("HH")
                            val hour = form.format(Date()).toInt()
                            putExtra("hour", hour)
                            putExtra("list", entity.toIntArray())
                            startActivity(this)
                        }
                    }
                })
        }
        // 登出
        logout.setOnClickListener {
            if (now_seat != 0) alert("占座中请不要退出账号哦！") { positiveButton("确定") {} }.show()
            else {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
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
                    else if (login_state.text == "空闲"){
                        // 当前扫码结果记为seatID
                        val seatID = obj.getOriginalValue().toInt()
                        val form = SimpleDateFormat("HH")
                        val hour = form.format(Date()).toInt()
                        Okkt.instance.Builder().setUrl("/seatwithreservation/tryoccupyseat")
                            .setParams(hashMapOf("userid" to userID.toString(),
                                "seatid" to seatID.toString(), "now" to hour.toString()))
                            .post(object: CallbackRule<String> {
                                override suspend fun onFailed(error: String) {
                                    alert("请检查网络") {
                                        setTitle("扫码失败")
                                        positiveButton("确定") {}
                                    }.show()
                                }
                                override suspend fun onSuccess(entity: String, flag: String) {
                                    if (entity == "Succeed") { // 成功
                                        // 跳转至占座页面
                                        alert("享受学习的乐趣吧！") {
                                            title = "占座成功"
                                            positiveButton("确定") {
                                                Intent(this@MainActivity, StudyActivity::class.java).apply {
                                                    putExtra("userID", userID)
                                                    putExtra("now_seat", seatID)
                                                    startActivity(this)
                                                }
                                            }
                                        }.show()
                                    }
                                    else if (entity == "Occupied") { // 座位被占
                                        alert("座位被占用了，请联系管理员") { positiveButton("确定") {} }.show()
                                    }
                                    else if (entity == "Booked") {
                                        alert("座位被预订了") { positiveButton("确定") {} }.show()
                                    }
                                    else if (entity == "Leave") {
                                        alert("座位的主人暂时离开了") {
                                            positiveButton("换个座位") { newViewBtnClick(main_to_scan) }
                                        }.show()
                                    }
                                    else {
                                        alert("注意：该座位在 $entity 点被预订！") {
                                            title = "占座成功"
                                            positiveButton("确定"){
                                                Intent(this@MainActivity, StudyActivity::class.java).apply {
                                                    putExtra("userID", userID)
                                                    putExtra("now_seat", seatID)
                                                    startActivity(this)
                                                }
                                            }
                                        }.show()
                                    }
                                }
                            })
                        /*
                        // 先判断扫到的座位是否为当前用户的预订，若是，则更新座位状态
                        val ordered_seat = 1011
                        if (seatID == ordered_seat) {
                            // 更新座位状态
                        }
                        else {  // 若否，判断扫到座位是否可用，若是，则成功占位
                            // 查询seat_list
                            Okkt.instance.Builder().setUrl("/seat/getspareseats/now")
                                .post(object : CallbackRule<MutableList<Int>> {
                                    override suspend fun onFailed(error: String) {
                                        alert("图书馆未开放！") { positiveButton("确定") {} }.show()
                                    }
                                    override suspend fun onSuccess(entity: MutableList<Int>, flag: String) {
                                        val seat_list = entity.toIntArray()
                                        if (seatID in seat_list) { // 成功找到空位，占座
                                            // 更改user、seat状态
                                            Okkt.instance.Builder().setUrl("/user/revisestatusbyid")
                                                .setParams(hashMapOf("userid" to userID.toString()))
                                                .putBody(hashMapOf("status" to UserStatus.ACTIVE.toString()))
                                                .post(object: CallbackRule<String> {
                                                    override suspend fun onFailed(error: String) { }
                                                    override suspend fun onSuccess(entity: String, flag: String) { }
                                                })
                                            Okkt.instance.Builder().setUrl("/seat/setseatoccupied")
                                                .setParams(hashMapOf("seatid" to seatID.toString()))
                                                .post(object: CallbackRule<String> {
                                                    override suspend fun onFailed(error: String) { }
                                                    override suspend fun onSuccess(entity: String, flag: String) { }
                                                })
                                            // 跳转至占座页面
                                            alert("成功占座") { positiveButton("确定") {
                                                Intent(this@MainActivity, StudyActivity::class.java).apply {
                                                    putExtra("userID", userID)
                                                    putExtra("seatID", seatID)
                                                    // user状态
                                                    startActivity(this)
                                                }
                                            } }.show()
                                        }
                                        else {// 检查是否暂离
                                            Okkt.instance.Builder().setUrl("/findbyid")
                                                .setParams(hashMapOf("seatid" to seatID.toString()))
                                                .get(object: CallbackRule<Seat> {
                                                    override suspend fun onFailed(error: String) {
                                                        toast("查询失败，请检查网络")
                                                    }
                                                    override suspend fun onSuccess(entity: Seat, flag: String) {
                                                        if (entity.status == SeatStatus.LEAVE.toString()) { // 暂离
                                                            alert("座位的主人暂时离开了！") {
                                                                positiveButton("换一个座位") { newViewBtnClick(main_to_scan) }
                                                            }.show()
                                                        }
                                                        else {
                                                            alert("该座位已被占用！") {
                                                                positiveButton("重新扫码") { newViewBtnClick(main_to_scan) }
                                                            }.show()
                                                        }
                                                    }
                                                })
                                        }
                                    }
                                })
                        }

                         */
                    }
                }
            }
        }

    }
    /**扫码实现结束*/

}

