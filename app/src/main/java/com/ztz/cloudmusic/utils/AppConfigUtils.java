package com.ztz.cloudmusic.utils;

import android.content.Context;

/**
 * 单例- 只存在一个对象
 * 1. 私有化构造方法
 * 2. 持有本类的应用
 * 3. 提供一个静态的方法，获取本类对象
 *
 * 这个app在运行的过程中，应该只存在一个AppConfigUtils对象
 * app　配置信息的类
 * 调用该类的方法可以很方便获取到
 * 1. 是否第一次使用应用
 * 2. 是否登录
 * Created by wqewqe on 2017/6/7.
 */

public class AppConfigUtils {
    /**
     *  静态方法可以直接通过类名调用，任何的实例也都可以调用，因此静态方法中不能用this和super关键字，
     *  不能直接访问所属类的实例变量和实例方法(就是不带static的成员变量和成员成员方法)，
     *  只能访问所属类的静态成员变量和成员方法
     */
    static AppConfigUtils appConfigUtils;
    static String GUIDE="guide";
    private AppConfigUtils(){}

    /**
     * static final用来修饰成员变量和成员方法，可简单理解为“全局常量”！
     对于变量，表示一旦给值就不可修改，并且通过类名可以访问。
     对于方法，表示不可覆盖，并且可以通过类名直接访问。
     * @return
     */
    public static final AppConfigUtils getInstance(){
        if(appConfigUtils==null){
            appConfigUtils=new AppConfigUtils();
        }
        return appConfigUtils;
    }

    /**
     * 设置是否第一次使用
     *
     * @param context 上下文
     * @param guide   是否第一次使用， true 是   false 否
     */
    public  boolean getGuide(Context context){
        return SpUtils.getBoolean(context,GUIDE);
    }
    public  void setGuide(Context context,boolean value){
        SpUtils.putBoolean(context,GUIDE,value);
    }
}
