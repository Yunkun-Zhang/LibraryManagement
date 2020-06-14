package com.example.librarymanagement.control;

import android.net.Uri;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

public class setUserInfo {

    public void setUserInfo(){
        //清除现有缓存
        RongUserInfoManager.getInstance().uninit();

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {
                //通过userid在数据库中获取到name，url，再写入
                //接下来只是测试
                String name;
                String url;
                if(userId.equals("123")) {
                    name = "张三";
                    url = "https://s1.ax1x.com/2020/06/13/txCi01.th.jpg";
                }
                else{
                    name = "王五";
                    url = "https://s1.ax1x.com/2020/06/13/tvr7S1.jpg";
                }
                return new UserInfo(userId, name, Uri.parse(url));
            }

        }, true);
    }

}
