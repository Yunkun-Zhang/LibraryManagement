import android.util.Log
import com.example.librarymanagement.model.User
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import splitties.mainhandler.mainHandler

const val USER_ID = 1

class UserControl {
    val baseURL = "/user" // 意义是关联user类，毕竟是服务器访问，实际用处不大

    /*
    val handler2 : Handler = Handler{
        when(it.what){
            USER_ID ->{
                try {
                    var t = it.obj
                } catch (ex : Throwable){
                    ex.printStackTrace()
                }
            }
            else -> {
            }
        }
        false
    } */

    // 注册函数，返回值为用户的id
    fun register(name: String, password: String){
        addUser(name, password)
    }

    // 登陆函数，返回值为用户id
    fun login(name: String, password: String){
        Okkt.instance.Builder().setUrl("/user/findbyname")
            .setParams(hashMapOf("name" to name)).get(object: CallbackRule<User> {
                override suspend fun onFailed(error: String) {
                    // TODO: add actions
                    Log.w("login", "name not found")
                }

                override suspend fun onSuccess(entity: User, flag: String) {
                    Log.w("login", "found password on server ${entity.password}")
                    if (password == entity.password) {
                        var id = entity.userID
                        var msg = mainHandler.obtainMessage()
                        msg.obj = id
                        msg.what = 200
                        mainHandler.sendMessage(msg)
                        Log.w("id_in_http", id.toString())
                        Log.w("successful login", "password: ${entity.password}")
                    }
                    else {
                        // TODO: add actions
                        Log.w("login error", "false password")
                    }
                }
            })
    }

    private fun addUser(name:String, password: String){
        Okkt.instance.Builder().setUrl("/user/add")
            .putBody(hashMapOf("name" to name, "password" to password))
            .post(object : CallbackRule<Int> {
                override suspend fun onFailed(error: String) {
                    Log.w("registration", "failed")
                }

                override suspend fun onSuccess(entity: Int, flag: String) {
                    Log.w("registration", "success")
                }
            })

    }

    private fun getUserByName(name: String): User? {
        var user: User? = null
        Okkt.instance.Builder().setUrl("/user/findbyname")
            .setParams(hashMapOf("name" to name)).get(object: CallbackRule<User> {
                override suspend fun onFailed(error: String) {
                    Log.w("login", "name not found")
                }

                override suspend fun onSuccess(entity: User, flag: String) {
                    Log.w("login", "found password on server ${entity.password}")
                    user = entity
                }
            })
        return user
    }

    private fun reviseUserInfo(userID: Int, name: String) {
        Okkt.instance.Builder().setUrl("/user/reviseinfobyid").putBody(hashMapOf("name" to name))
            .post(object: CallbackRule<String> {
                override suspend fun onFailed(error: String) {
                    TODO("Not yet implemented")
                }

                override suspend fun onSuccess(entity: String, flag: String) {
                    TODO("Not yet implemented")
                }

            })
    }

}
