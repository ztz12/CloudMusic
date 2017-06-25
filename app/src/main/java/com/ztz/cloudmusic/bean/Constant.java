package com.ztz.cloudmusic.bean;

/**
 * 存储app中的常量
 * 比如：url链接
 * Created by wqewqe on 2017/6/9.
 */

public class Constant {
    /**
     * 存储所有app接口地址z
     */
    public static class URL{
        public final static String HOME="https://leancloud.cn:443/1.1/classes/Home";
        public final static String NEWPLAYLIST = "https://leancloud.cn:443/1.1/classes/NewPlayList";
        public final static String PLAYLIST="https://leancloud.cn:443/1.1/classes/PlayList";
    }
    public static class Action{
        //动作
        public final static String ACTION_PLAY = "com.jf.studentjfmusic.action_play";


        //通知-开始播放音乐的通知，在MusicService服务里面，如果调用了play方法就会发送该播放
        public final static String PLAY = "com.jf.studentjfmusic.play";

        //通知-暂停播放音乐的通知，
        public final static String PAUSE = "com.jf.studentjfmusic.pause";

        public final static String SEEK_PLAY="com.ztz.cloudmusic.play";

    }
}
