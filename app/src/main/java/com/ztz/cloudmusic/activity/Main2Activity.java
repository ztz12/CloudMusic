package com.ztz.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.fragment.FoundFragment;
import com.ztz.cloudmusic.fragment.FriendFragment;
import com.ztz.cloudmusic.fragment.MyFragment;
import com.ztz.cloudmusic.service.MusicService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Parcelable和Serializable的区别：
     * android自定义对象可序列化有两个选择一个是Serializable和Parcelable
     * 一、对象为什么需要序列化
     * 1.永久性保存对象，保存对象的字节序列到本地文件。
     * 2.通过序列化对象在网络中传递对象。
     * 3.通过序列化对象在进程间传递对象。
     * 二、当对象需要被序列化时如何选择所使用的接口
     * 1.在使用内存的时候Parcelable比Serializable的性能高。
     * 2.Serializable在序列化的时候会产生大量的临时变量，从而引起频繁的GC（内存回收）。
     * 3.Parcelable不能使用在将对象存储在磁盘上这种情况，因为在外界的变化下Parcelable不能很好的保证数据的持续性。
     */

    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    private static final String TAG = "Main2Activity";
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.my)
    ImageView my;
    @BindView(R.id.found)
    ImageView found;
    @BindView(R.id.friend)
    ImageView friend;
    @BindView(R.id.iv_play_Status)
    ImageView ivPlayStatus;
    @BindView(R.id.tv_name_play)
    TextView tvNamePlay;

    private ViewPager vp_main;
    List<Fragment> fragmentList = new ArrayList<>();
    private MainAdapter adapter1;
    List<ImageView> imageViewList;
    private PlayBroadCast playBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        imageViewList = new ArrayList<>();
        imageViewList.add(my);
        imageViewList.add(found);
        imageViewList.add(friend);

        fragmentList.add(new MyFragment());
        fragmentList.add(new FoundFragment());
        fragmentList.add(new FriendFragment());
        adapter1 = new MainAdapter(getSupportFragmentManager());
        vp_main.setAdapter(adapter1);
        vp_main.setCurrentItem(1);
        found.setSelected(true);
        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLayout.openDrawer(GravityCompat.START);
            }
        });
        my.setOnClickListener(this);
        found.setOnClickListener(this);
        friend.setOnClickListener(this);
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                switch (position){
                setSelectPosition(position);
//                    case 0:
//                        my.setSelected(true);
//                        found.setSelected(false);
//                        friend.setSelected(false);
////                        my.setImageResource(R.mipmap.actionbar_music_prs);
////                        found.setImageResource(R.mipmap.actionbar_discover_normal);
////                        friend.setImageResource(R.mipmap.actionbar_friends_normal);
//                        break;
//                    case 1:
//                        my.setSelected(false);
//                        found.setSelected(true);
//                        friend.setSelected(false);
////                        my.setImageResource(R.mipmap.actionbar_music_normal);
////                        found.setImageResource(R.mipmap.actionbar_discover_prs);
////                        friend.setImageResource(R.mipmap.actionbar_friends_normal);
//                        break;
//                    case 2:
//                        my.setSelected(false);
//                        found.setSelected(false);
//                        friend.setSelected(true);
////                        my.setImageResource(R.mipmap.actionbar_music_normal);
////                        found.setImageResource(R.mipmap.actionbar_discover_normal);
////                        friend.setImageResource(R.mipmap.actionbar_friends_prs);
//                        break;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //获取手机屏幕密度
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        registerBroadCast();
        bindMusicService();
    }

    private void registerBroadCast() {
        playBroadCast = new PlayBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.PLAY);
        intentFilter.addAction(Constant.Action.PAUSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(playBroadCast, intentFilter);
    }

    @OnClick(R.id.iv_play_Status)
    public void onViewClicked() {
        if (musicBinder.isPlaying()) {
            musicBinder.pause();
            ivPlayStatus.setImageResource(R.mipmap.a2s);
        } else {
            musicBinder.play();
            ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }

    class PlayBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Constant.Action.PLAY:
                    ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
                    break;
                case Constant.Action.PAUSE:
                    ivPlayStatus.setImageResource(R.mipmap.a2s);
                    break;
            }

        }
    }

    MusicService.MusicBinder musicBinder;

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            if(musicBinder.isPlaying()){
                ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
            }else {
                ivPlayStatus.setImageResource(R.mipmap.a2s);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void setSelectPosition(int position) {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i == position) {
                imageViewList.get(i).setSelected(true);
            } else {
                imageViewList.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my:
                vp_main.setCurrentItem(0);
                break;
            case R.id.found:
                vp_main.setCurrentItem(1);
                break;
            case R.id.friend:
                vp_main.setCurrentItem(2);
                break;
        }
    }

    class MainAdapter extends FragmentPagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(playBroadCast);
        unbindService(serviceConnection);
    }
}
