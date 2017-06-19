package com.ztz.cloudmusic.utils;

/**
 * 项目String 工具类
 * Created by wqewqe on 2017/6/5.
 */

public class AppStringUtils {
    /**
     * 检查用户名是否正确
     * @param userName
     * @return true 正确， false 错误
     */
    public static boolean checkUserName(String userName){
        if(userName==null){
            return false;
        }
        if("".equals(userName)){
            return false;
        }
        if(userName.length()<3||userName.length()>10){
            return false;
        }
        return true;
    }

    /**
     * 检查密码是否合法
     * @param password
     * @return true 正确， false 错误
     */
    public static boolean checkPassword(String password){
        if(password==null){
            return false;
        }
        if("".equals(password)){
            return false;
        }
        if(password.length()<3||password.length()>10){
            return false;
        }
        return true;
    }
}
