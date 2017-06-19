package com.ztz.cloudmusic.bean;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class Url {
    /**
     * Context数量 = Activity数量 + Service数量 + 1
     * 上面的1代表着Application的数量，因为一个应用程序中可以有多个Activity和多个Service，但是只能有一个Application
     *
     * 这里我快速连续点击了五次按钮，Toast就触发了五次。这样的体验其实是不好的，
     * 因为也许用户是手抖了一下多点了几次，导致Toast就长时间关闭不掉了。又或者我们其实已在进行其他操作了，
     * 应该弹出新的Toast提示，而上一个Toast却还没显示结束
     */
    private static Toast toast;
    public static void showToast(Context context,String content){
        if(toast==null){
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
