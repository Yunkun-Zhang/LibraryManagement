package com.example.librarymanagement.control

import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.model.User

class UserControl {
    var uDao = AppDataBase.instance.getUserDao()

    // 注册效果：数据库中添加一项
    fun register(nickname:String, password:String) {
        if (uDao.getByNickname(nickname) == null) {
            val id = uDao.getMAXUserID()
            uDao.insert(User(id+1, nickname, password, null, null, null))
        }
    }

    // 登陆：匹配则返回用户id，否则返回0
    fun login(nickname: String, password: String): Int? {
        val user = uDao.getByNickname(nickname)
        if (password == user!![0].password) {
            return user!![0].userID
        }
        return 0
    }


}