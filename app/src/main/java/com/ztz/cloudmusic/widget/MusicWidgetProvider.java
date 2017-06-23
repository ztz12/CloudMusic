package com.ztz.cloudmusic.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.ztz.cloudmusic.R;

/**
 * Created by wqewqe on 2017/6/23.
 */

public class MusicWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "MusicWidgetProvider";
    public static final String WIDGET_LAST="com.ztz.cloudmusic.WIDGET_LAST";
    public static final String WIDGET_PLAY="com.ztz.cloudmusic.WIDGET_PLAY";
    public static final String WIDGET_NEXT="com.ztz.cloudmusic.WIDGET_NEXT";

    public static final String WIDGET_LAST_ACTION = "com.ztz.cloudmusic.WIDGET_LAST_ACTION";
    public static final String WIDGET_PLAY_ACTION = "com.ztz.cloudmusic.WIDGET_PLAY_ACTION";
    public static final String WIDGET_NEXT_ACTION = "com.ztz.cloudmusic.WIDGET_NEXT_ACTION";

    //每接受一次广播消息就调用
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive: ");
        String action=null;
        switch (intent.getAction()){
            case WIDGET_LAST:
                action=WIDGET_LAST_ACTION;
                break;
            case WIDGET_PLAY:
//                Toast.makeText(context, "播放", Toast.LENGTH_SHORT).show();
                action=WIDGET_PLAY_ACTION;
                break;
            case WIDGET_NEXT:
//                Toast.makeText(context, "播放下一首", Toast.LENGTH_SHORT).show();
                action=WIDGET_NEXT_ACTION;
                break;
        }
        Intent i=new Intent();
        i.setAction(action);
        context.sendBroadcast(i);
    }
    //每更新一次就调用该方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate: ");
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intentLast=new Intent(WIDGET_LAST);
        PendingIntent pendingIntentLast=PendingIntent.getBroadcast(context,0,intentLast,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_last,pendingIntentLast);
        Intent intentPlay=new Intent(WIDGET_PLAY);
        PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_play,pendingIntentPlay);
        Intent intentNext=new Intent(WIDGET_NEXT);
        PendingIntent pendingIntentNext=PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next,pendingIntentNext);
        //更新小控件信息
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
    }
    //每删除一个就调用一次
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i(TAG, "onDeleted: ");
    }
    //当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i(TAG, "onEnabled: ");
    }
    //当最后一个该Widget删除是调用该方法，注意是最后一个
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.i(TAG, "onDisabled: ");
    }
}
