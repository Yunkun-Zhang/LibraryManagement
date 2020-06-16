package com.example.librarymanagement.control;

import android.net.Uri;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

public class SetUserInfo {

    public void setUserInfo(final String name){
        //清除现有缓存
        RongUserInfoManager.getInstance().uninit();

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {
                String url = "";
                return new UserInfo(userId, name, Uri.parse(url));
            }

        }, true);
    }

}
