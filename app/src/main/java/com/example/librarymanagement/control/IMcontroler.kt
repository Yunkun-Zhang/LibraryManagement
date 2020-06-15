package com.example.librarymanagement.control

import android.content.Context
import android.util.Log
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient.*
import io.rong.imlib.model.Message


class IMcontroler {
    private val context: Context? = null

    fun init(context: Context){
        initRongIM(context)
        initConnectStateChangeListener()
        initsetOnReceiveMessageListener(context)
    }

    private fun initRongIM(context:Context) {
        // 可在初始 SDK 时直接带入融云 IM 申请的APP KEY
        RongIM.init(context,"bmdehs6pba5ps")
    }

    private fun initConnectStateChangeListener() {
        RongIM.setConnectionStatusListener { connectionStatus ->
            connectionStatus.message
            if (connectionStatus == ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT ||
                connectionStatus == ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT) {
                //TODO token 错误时，重新登录
            }
        }
    }


    //消息监听器
    private fun initsetOnReceiveMessageListener(context: Context){
        RongIM.setOnReceiveMessageListener(object : OnReceiveMessageWrapperListener() {
            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */
            override fun onReceived(message: Message?,left: Int,hasPackage: Boolean,offline: Boolean): Boolean {
                Log.i("message",message.toString())
                return true
            }
        })
    }

    fun connect(taken:String){
        RongIM.connect(taken, object : ConnectCallbackEx() {
            /**
             * 数据库回调.
             * @param code 数据库打开状态. DATABASE_OPEN_SUCCESS 数据库打开成功; DATABASE_OPEN_ERROR 数据库打开失败
             */
            override fun OnDatabaseOpened(code: DatabaseOpenStatus) {}

            /**
             * token 无效
             */
            override fun onTokenIncorrect() {}

            /**
             * 成功回调
             * @param userId 当前用户 ID
             */
            override fun onSuccess(userId: String) {}

            /**
             * 错误回调
             * @param errorCode 错误码
             */
            override fun onError(errorCode: ErrorCode) {}
        })

    }
}