package com.ztz.cloudmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.PlayList;
import com.ztz.cloudmusic.widget.MusicWidgetProvider;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MusicService extends Service {
    public static MediaPlayer mp = new MediaPlayer();
    public static PlayList mPlayList;
    //获取播放的下标
    public static int mCurrIndex = 0;
    public MusicBinder musicBinder;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        musicBinder=new MusicBinder();
        return musicBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicReceiver();
        initNotification();
    }

    private void initMusicReceiver() {
        MusicReceiver musicReceiver=new MusicReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(MusicWidgetProvider.WIDGET_LAST_ACTION);
        intentFilter.addAction(MusicWidgetProvider.WIDGET_PLAY_ACTION);
        intentFilter.addAction(MusicWidgetProvider.WIDGET_NEXT_ACTION);
        registerReceiver(musicReceiver,intentFilter);
    }
    private void initNotification(){
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_notication);
        Intent intentLast=new Intent(MusicWidgetProvider.WIDGET_LAST_ACTION);
        PendingIntent pendingIntentLast=PendingIntent.getBroadcast(this,0,intentLast,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_last,pendingIntentLast);
        Intent intentPlay=new Intent(MusicWidgetProvider.WIDGET_PLAY_ACTION);
        PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(this,0,intentPlay,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_play,pendingIntentPlay);
        Intent intentNext=new Intent(MusicWidgetProvider.WIDGET_NEXT_ACTION);
        PendingIntent pendingIntentNext=PendingIntent.getBroadcast(this,0,intentNext,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next,pendingIntentNext);

        //CTRL + Alt + V  生成返回值
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        Notification notification=builder
                .setContentTitle("我是标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .build();
        startForeground(1,notification);
    }
    public class MusicBinder extends Binder {
        public void play() {
            mp.start();
            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MusicService.this);
            manager.sendBroadcast(intent);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateWidget(music);
            updateNotification(music);
            handler.sendMessageAtTime(Message.obtain(),500);
        }

        public void play(PlayList playList) {
//            if(mp==null){
//                mp=new MediaPlayer();
//                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mp.setLooping(false);
//            }
            mPlayList = playList;
            String url = "";
            for (int i = 0; i < playList.getMusics().size(); i++) {
                PlayList.Music music = playList.getMusics().get(i);
                if (music.isPlayStatus()) {
                    url = music.getMusicUrl();
                    mCurrIndex = i;
                }
            }
           playUrl(url);
        }
        public void play(int position){
            //获取当前播放的下标
            mCurrIndex=position;
            for(int i=0;i<mPlayList.getMusics().size();i++){
                mPlayList.getMusics().get(i).setPlayStatus(false);
            }
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            music.setPlayStatus(true);
            String url=music.getMusicUrl();
            playUrl(url);
        }
        public void playUrl(String url){
            mp.reset();
            try{
                mp.setDataSource(url);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
            Intent intent=new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
            PlayList.Music music=mPlayList.getMusics().get(mCurrIndex);
            updateWidget(music);
            updateNotification(music);
            handler.sendMessageAtTime(Message.obtain(),500);
        }

        public void pause() {
            mp.pause();
            Intent intent = new Intent(Constant.Action.PAUSE);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MusicService.this);
            manager.sendBroadcast(intent);
            PlayList.Music music = mPlayList.getMusics().get(mCurrIndex);
            updateWidget(music);
            updateNotification(music);
        }

        /**
         * 获取播放状态
         *
         * @return
         */
        public boolean isPlaying() {
            if (mp != null) {
                return mp.isPlaying();
            } else {
                return false;
            }
        }
    }

    /**
     * 获取当前播放的歌单
     *
     * @return
     */
    public static PlayList getCurrPlay() {
        return mPlayList;
    }

    /**
     * 获取当前播放歌曲的下标值
     *
     * @return
     */
    public static int getCurrIndex() {
        return mCurrIndex;
    }
    /**
     * 获取当前播放进度
     */
    public static int getCurrPosition(){
        if(mp!=null){
            return mp.getCurrentPosition();
        }
        return 0;
    }
    //
    public static int getDuration(){
        if(mp!=null){
            return mp.getDuration();
        }
        return 0;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(musicBinder.isPlaying()){
                updateWidget(mPlayList.getMusics().get(mCurrIndex));
                updateNotification(mPlayList.getMusics().get(mCurrIndex));
            }
            handler.sendMessageDelayed(Message.obtain(),500);
        }
    };
    public void updateWidget(final PlayList.Music music) {
        if (music != null) {
            final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_content, music.getTitle());
            if (musicBinder.isPlaying()) {
                remoteViews.setImageViewResource(R.id.widget_play, R.mipmap.tr);
            } else {
                remoteViews.setImageViewResource(R.id.widget_play, R.mipmap.b13);
            }
            remoteViews.setProgressBar(R.id.widget_progress, getDuration(), getCurrPosition(), false);
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap=Glide.with(MusicService.this)
                                .load(music.getAlbumPicUrl())
                                .asBitmap()
                                .centerCrop()
                                .into(150,150)
                                .get();
                        remoteViews.setImageViewBitmap(R.id.widget_image,bitmap);
                        appWidgetManager.updateAppWidget(new ComponentName(MusicService.this, MusicWidgetProvider.class), remoteViews);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public void updateNotification(final PlayList.Music music){
        if(music!=null) {
            final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notication);
            remoteViews.setTextViewText(R.id.widget_content, music.getTitle());
            if (musicBinder.isPlaying()) {
                remoteViews.setImageViewResource(R.id.widget_play, R.mipmap.tr);
            } else {
                remoteViews.setImageViewResource(R.id.widget_play,R.mipmap.b13);
            }
            remoteViews.setProgressBar(R.id.widget_progress, getDuration(), getCurrPosition(), false);
            final NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
            final Notification notification=builder
                    .setContentTitle("我是标题")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(remoteViews)
                    .build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap= Glide.with(MusicService.this)
                                .load(music.getAlbumPicUrl())
                                .asBitmap()
                                .centerCrop()
                                .into(100,100)
                                .get();
                        remoteViews.setImageViewBitmap(R.id.widget_image,bitmap);
                        manager.notify(1,notification);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    class MusicReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MusicWidgetProvider.WIDGET_LAST_ACTION:
                    if(mCurrIndex==0){
                        Toast.makeText(context, "第一首歌啦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurrIndex=mCurrIndex-1;
                    musicBinder.play(mCurrIndex);
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);
                    updateNotification(mPlayList.getMusics().get(mCurrIndex));
                    break;
                case MusicWidgetProvider.WIDGET_PLAY_ACTION:
                    if(musicBinder.isPlaying()){
                        musicBinder.pause();
                    }else {
                        musicBinder.play();
                    }
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);
                    updateNotification(mPlayList.getMusics().get(mCurrIndex));
                    break;
                case MusicWidgetProvider.WIDGET_NEXT_ACTION:
                    if(mCurrIndex==mPlayList.getMusics().size()-1){
                        Toast.makeText(context, "最后一首歌啦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurrIndex=mCurrIndex+1;
                    musicBinder.play(mCurrIndex);
                    updateWidget(mPlayList.getMusics().get(mCurrIndex));
                    handler.sendMessageAtTime(Message.obtain(),500);
                    updateNotification(mPlayList.getMusics().get(mCurrIndex));
                    break;

            }
        }
    }
}
