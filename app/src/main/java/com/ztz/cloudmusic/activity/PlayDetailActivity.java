package com.ztz.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.LrcBeen;
import com.ztz.cloudmusic.bean.PlayList;
import com.ztz.cloudmusic.service.MusicService;
import com.ztz.cloudmusic.widget.DiscView;
import com.ztz.cloudmusic.widget.LrcView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayDetailActivity extends AppCompatActivity {
    public static final String DETAIL_KEY = "details";
    public static final String RESULTBEAN_KEY = "resultsBeanList";
    public static final String INDEX_KEY = "position";

    private static final String TAG = "PlayDetailActivity";
    @BindView(R.id.iv_detail_bg)
    ImageView ivDetailBg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_actionBar)
    LinearLayout llActionBar;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.disv)
    DiscView disv;
    @BindView(R.id.tv_PName)
    TextView tvPName;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    //    ArrayList<PlayListResponse.ResultsBean> resultsBeanList;
    //歌单歌曲列表
    PlayList mPlayList;
    @BindView(R.id.iv_last)
    ImageView ivLast;
    @BindView(R.id.iv_now)
    ImageView ivNow;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.lrcView)
    LrcView lrcView;
    public static Handler handlerMsg;
    public static SeekBar seekBar;
    private PlayList.Music music;
    private SeekReceiver receiver;
    //    private PlayListResponse.ResultsBean resultsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        ButterKnife.bind(this);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        mPlayList = getIntent().getParcelableExtra(RESULTBEAN_KEY);
        Log.i(TAG, "onCreate: " + mPlayList);
//        resultsBean = getIntent().getParcelableExtra(DETAIL_KEY);
//        Log.i(TAG, "onCreate: " + resultsBean);
        //获取下标
        int position = getIntent().getIntExtra(INDEX_KEY, 0);
        music = mPlayList.getMusics().get(position);
        Log.i(TAG, "onCreate: " + position);
        String url = "http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg";
        if (music.getAlbumPicUrl() != null) {
            url = music.getAlbumPicUrl();
        }
        tvPName.setText(music.getTitle());
        tvArtist.setText(music.getArtist());
        Glide.with(this).load(url)
                //模糊图片,  10 模糊度   5 将图片缩放到5倍后进行模糊
                .bitmapTransform(new BlurTransformation(this, 10, 5))
                .into(ivDetailBg);
        disv.setDisChange(new DiscView.DiscChangeListener() {
            @Override
            public void onActionBarChange(PlayList.Music bean) {
                tvPName.setText(bean.getTitle());
                tvArtist.setText(bean.getArtist());
            }

            @Override
            public void Next(final int position) {
                //播放下一首
//                Toast.makeText(PlayDetailActivity.this, "下一首", Toast.LENGTH_SHORT).show();
                if (binder != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            binder.play(position);
                        }
                    }).start();

                }
            }

            @Override
            public void Last(final int position) {
                //播放上一首
//                Toast.makeText(PlayDetailActivity.this, "上一首", Toast.LENGTH_SHORT).show();
                if (binder != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            binder.play(position);
                        }
                    }).start();
                }
            }

            @Override
            public void onItemClick() {
                lrcView.setVisibility(View.VISIBLE);
                disv.setVisibility(View.GONE);
                if (lrcList.isEmpty()) {
                    downloadLrc(music.getLrcUrl());
                } else {
                    lrcView.setLrcData(lrcList);
                }
            }
        });
        disv.setMusicData(mPlayList, position);
        bindMusicService();
        lrcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lrcView.setVisibility(View.GONE);
                disv.setVisibility(View.VISIBLE);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binder.seekWait();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                binder.seekTo(progress);
                binder.seekNotify();
            }
        });
        /**
         * 如果一个线程中调用Looper.prepare()，那么系统就会自动的为该线程建立一个消息队列，
         * 然后调用 Looper.loop();之后就进入了消息循环，这个之后就可以发消息、取消息、和处理消息
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                handlerMsg=new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        switch (msg.what){
//                            case 1:
//                                int duration=msg.arg1;
//                                int progress=msg.arg2;
//                                seekBar.setMax(duration);
//                                seekBar.setProgress(progress);
//                        }
//                    }
//                };
//                Looper.loop();
//            }
//        }).start();
        registerSeek();
    }
    public void registerSeek(){
        receiver = new SeekReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.Action.SEEK_PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,intentFilter);

    }
    class SeekReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int duration=MusicService.getDuration();
            int progress=MusicService.getCurrPosition();
            seekBar.setMax(duration);
            seekBar.setProgress(progress);
        }
    }

    /**
     * 下载歌词
     *
     * @param lrcUrl
     */
    private void downloadLrc(String lrcUrl) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(lrcUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //每一次都需要去请求歌词，可以将歌词存储到文件里面，
                //下一次请求，判断是否存在该文件，如果存在，则直接使用
                String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                parseLrc(result);
            }
        });
    }

    ArrayList<LrcBeen> lrcList = new ArrayList<>();

    /**
     * 解析歌词
     *
     * @param result
     */
    private void parseLrc(String result) {
        String[] split = result.split("\n");
        Log.i(TAG, "parseLrc: " + Arrays.toString(split));
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            //[00:00.00]
            String[] arr = line.split("\\]");
            //分钟
            String min = arr[0].split(":")[0].replace("[", "");
            //秒
            String sec = arr[0].split(":")[1].split("\\.")[0];
            //毫秒
            String desc = arr[0].split(":")[1].split("\\.")[1];
            Log.i(TAG, "parseLrc: " + min + " " + sec + " " + desc);
            String content;
            if (arr.length > 1) {
                content = arr[1];
            } else {
                content = "music";
            }
            //获取开始时间
            long startTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(sec) * 1000 + Long.valueOf(desc);
            //将获取的数据封装成对象
            LrcBeen lrcBeen = new LrcBeen(content, startTime, 0);
            lrcList.add(lrcBeen);
            if (lrcList.size() > 1) {
                lrcList.get(lrcList.size() - 2).setEndTime(startTime);
            }
            if (i == split.length - 1) {
                lrcList.get(lrcList.size() - 1).setEndTime(startTime + 1000000);
            }
        }
        Log.e(TAG, "parseLrc: " + lrcList.toString());
    }


    MusicService.MusicBinder binder;

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MusicBinder) service;
            //服务器连接成功 判断按钮播放状态
            setPlayStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 改变播放状态
     *
     * @param view
     */
    public void setPlayStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (binder.isPlaying()) {
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_pause);
                    disv.setMusicStatus(DiscView.MusicStatus.MUSIC_PLAY);
                    disv.playAnim();
                } else {
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_play);
                    disv.setMusicStatus(DiscView.MusicStatus.MUSIC_PAUSE);
                    disv.pauseAnim();
                }
            }
        });
    }

    @OnClick({R.id.iv_last, R.id.iv_now, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                disv.playLast();
                ivNow.setImageResource(R.mipmap.play_rdi_btn_pause);
                break;
            case R.id.iv_now:
                if (binder.isPlaying()) {
                    binder.pause();
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_play);
                    disv.pauseAnim();
                } else {
                    binder.play();
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_pause);
                    disv.playAnim();
                }
                break;
            case R.id.iv_next:
                disv.playNext();
                ivNow.setImageResource(R.mipmap.play_rdi_btn_pause);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
