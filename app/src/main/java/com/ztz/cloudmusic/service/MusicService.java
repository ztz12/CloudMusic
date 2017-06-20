package com.ztz.cloudmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.PlayList;

import java.io.IOException;

public class MusicService extends Service {
    public static MediaPlayer mp = new MediaPlayer();
    public static PlayList mPlayList;
    public static int mCurrIndex = 0;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MusicBinder();
    }

    public class MusicBinder extends Binder {
        public void play() {
            mp.start();
            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MusicService.this);
            manager.sendBroadcast(intent);
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
            mp.reset();
            try {
                mp.setDataSource(url);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mp.start();
            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MusicService.this);
            manager.sendBroadcast(intent);
        }

        public void pause() {
            mp.pause();
            Intent intent = new Intent(Constant.Action.PAUSE);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MusicService.this);
            manager.sendBroadcast(intent);
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
}
