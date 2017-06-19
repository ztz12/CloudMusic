package com.ztz.cloudmusic.activity.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.service.MusicService;

/**
 * Created by wqewqe on 2017/6/16.
 */

//public abstract class BaseActivity extends AppCompatActivity {
public class BaseActivity extends AppCompatActivity {
    public static String TAG = "BaseActivity";
    public  ImageView iv_playStatus;
    public  MusicBroadCastReceiver musicReceiver;
    //    //方式一
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//        FrameLayout root=(FrameLayout)findViewById(R.id.root);
//        //获取子类布局
//        int rootId=layoutResID();
//        //加载子类布局文件
//        View childView= LayoutInflater.from(this).inflate(rootId,root,false);
//        //将子类view 添加到父类view中
//        root.addView(childView);
//
//
//    }

    /**
     * 返回想要显示的布局
     *
     * @param layoutResID
     */
//    public abstract int layoutResID();

    //方式二
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //加载父类布局
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        initBottomView(view);
        FrameLayout root = (FrameLayout) view.findViewById(R.id.root);
        //加载子类布局
        View childView = LayoutInflater.from(this).inflate(layoutResID, root, false);
        root.addView(childView);
        setContentView(view);
        //获取类型名字
        TAG = getClass().getName();
        register();
        bindMusicService();
    }

    private void initBottomView(View bottomView) {
        iv_playStatus = (ImageView) bottomView.findViewById(R.id.iv_play_Status);
        iv_playStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBinder.isPlaying()) {
                    musicBinder.pause();
                    iv_playStatus.setImageResource(R.mipmap.a2s);
                } else {
                    musicBinder.play();
                    iv_playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
                }
            }
        });
    }

    private void register() {
        musicReceiver = new MusicBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //注册两个广播
        intentFilter.addAction(Constant.Action.PLAY);
        intentFilter.addAction(Constant.Action.PAUSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(musicReceiver, intentFilter);
    }

    class MusicBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果接收多个广播 需判断广播类型
            switch (intent.getAction()) {
                case Constant.Action.PLAY:
                    iv_playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
                    break;
                case Constant.Action.PAUSE:
                    iv_playStatus.setImageResource(R.mipmap.a2s);
            }
        }
    }

    public MusicService.MusicBinder musicBinder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            //进入到界面 判断当前播放状态
            if(musicBinder.isPlaying()){
                iv_playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
            }else {
                iv_playStatus.setImageResource(R.mipmap.a2s);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁，需要解绑服务和广播
        //unregisterReceiver(mPlayBroadcastReceiver);//本地广播不能使用这种方式解绑
        LocalBroadcastManager.getInstance(this).unregisterReceiver(musicReceiver);
        unbindService(connection);
    }
}
