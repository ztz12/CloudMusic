package com.ztz.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.activity.base.BaseActivity;
import com.ztz.cloudmusic.adapter.PlayListAdapter;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.HomeResponse;
import com.ztz.cloudmusic.bean.PlayList;
import com.ztz.cloudmusic.bean.PlayListResponse;
import com.ztz.cloudmusic.service.MusicService;
import com.ztz.cloudmusic.utils.HttpUtils;
import com.ztz.cloudmusic.utils.JFMusicUrlJoint;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayListActivity extends BaseActivity {
    /**
     * 匹配正在播放的歌单
     * 1. 需要获取到正在播放的歌单的id
     * 2. 如果播放的歌单id 和 进入界面的歌单id一致 ，需要获取正在播放的歌曲下标
     */
    private static final String TAG = "PlayListActivity";
    public static final String PLAYLISTBEAN_KEY = "playListBean";
    public static final String AUTHOR_KEY = "author";
    @BindView(R.id.tv_name_play)
    TextView tvNamePlay;
    @BindView(R.id.iv_play_Status)
    ImageView ivPlayStatus;
    private HomeResponse.ResultsBean.PlayListBean playListBean;
    ArrayList<PlayListResponse.ResultsBean> resultsBeanList = new ArrayList<>();
    RecyclerView rl_play;
    private PlayListAdapter adapter;
    private RelativeLayout ll_toolBar;
    private TextView tv_playListName;
    private TextView tv_author;
    private TextView tv_title;
    private SmartImageView smartImageView;
    private ImageView iv_playBack;
    ImageView iv_bg;
    ImageView iv_list;
    ImageView iv_play_bg;
    private PlayBroadCast broadCast;
    //歌单对象
    PlayList mPlayList = new PlayList();
    public static SeekBar seekBar;
    private SeekPlayReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        seekBar = (SeekBar) findViewById(R.id.sBar);
        playListBean = getIntent().getParcelableExtra(PLAYLISTBEAN_KEY);
//        String objected=getIntent().getStringExtra(PLAYLISTBEAN_KEY);
        Log.e(TAG, "onCreate: " + playListBean);
        String author = getIntent().getStringExtra(AUTHOR_KEY);
        getData(playListBean.getObjectId());
        ll_toolBar = (RelativeLayout) findViewById(R.id.ll_toolbar);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        tv_title = (TextView) findViewById(R.id.tv_title);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ll_toolBar.getLayoutParams();
        layoutParams.height = layoutParams.height + getStatusBarHeight(this);
        ll_toolBar.setLayoutParams(layoutParams);
        rl_play = (RecyclerView) findViewById(R.id.rl_play);
        rl_play.setLayoutManager(new LinearLayoutManager(this));
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_playlist_header, rl_play, false);
        tv_playListName = (TextView) headView.findViewById(R.id.tv_playListName);
        tv_author = (TextView) headView.findViewById(R.id.tv_author);
        smartImageView = (SmartImageView) headView.findViewById(R.id.smart_play_view);
        iv_play_bg = (ImageView) headView.findViewById(R.id.iv_play_bg);
        //模糊背景
        Glide.with(this).load(playListBean.getPicUrl())
                //模糊图片, 10 模糊度   5 将图片缩放到5倍后进行模糊
                .bitmapTransform(new BlurTransformation(this, 10, 5))
                .into(iv_play_bg);
        adapter = new PlayListAdapter(mPlayList);
        adapter.setHeaderView(headView);
        rl_play.setAdapter(adapter);
        tv_playListName.setText(playListBean.getPlayListName());
        tv_author.setText(author);
        smartImageView.setImageUrl(playListBean.getPicUrl());
        LinearLayout ll = (LinearLayout) headView.findViewById(R.id.ll_play);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        params.topMargin = layoutParams.height + getStatusBarHeight(this);
        ll.setLayoutParams(params);
        iv_playBack = (ImageView) findViewById(R.id.iv_playBack);
        iv_playBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_play.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled: " + "dx: " + dx + " " + "dy: " + dy);
                View headView = null;
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int findFirstVisibleItemPosition = manager.findFirstVisibleItemPosition();//获取第一个显示的item的下标
                if (findFirstVisibleItemPosition == 0) {
                    headView = recyclerView.getChildAt(findFirstVisibleItemPosition);
                }
                float alpha = 0;//完全透明
                if (headView != null) {
                    alpha = Math.abs(headView.getTop() * 1.0f) / headView.getHeight();
                } else {
                    alpha = 1;//不透明0
                }
                if (alpha > 0.5) {
                    tv_title.setText(playListBean.getPlayListName());
                } else {
                    tv_title.setText("歌单");
                }
                iv_bg.setAlpha(alpha);
            }
        });
        registerBroadCast();
        registerSeek();
//        bindMusicService();
        iv_list = (ImageView) headView.findViewById(R.id.iv_list_play);
        iv_list.setColorFilter(Color.BLACK);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                musicBinder.seekWait();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                musicBinder.seekTo(progress);
                musicBinder.seekNotify();
            }
        });
    }
    public void registerSeek(){
        receiver = new SeekPlayReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constant.Action.SEEK_PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,intentFilter);

    }
    class SeekPlayReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int duration=MusicService.getDuration();
            int progress=MusicService.getCurrPosition();
            seekBar.setMax(duration);
            seekBar.setProgress(progress);
        }
    }
//    public static Handler handlerMsg = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    int duration = msg.arg1;
//                    int progress = msg.arg2;
//                    seekBar.setMax(duration);
//                    seekBar.setProgress(progress);
//            }
//        }
//    };
//
//    MusicService.MusicBinder binder;
//
//    private void bindMusicService() {
//        Intent intent = new Intent(PlayListActivity.this, MusicService.class);
//        bindService(intent, connection, BIND_AUTO_CREATE);
//    }

//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            binder = (MusicService.MusicBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    @Override
    public void musicStatusChange() {
        Log.i(TAG, "musicStatusChange: 我被调用了");
        PlayList currPlayList = MusicService.getCurrPlay();
        if (mPlayList.getObjectId().equals(currPlayList.getObjectId())) {
            mPlayList.setMusics(currPlayList.getMusics());
            adapter.notifyDataSetChanged();
        }
    }

    //注册广播
    private void registerBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.ACTION_PLAY);
        broadCast = new PlayBroadCast();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCast, intentFilter);
    }

//    @OnClick(R.id.iv_play_Status)
//    public void onViewClicked(View view) {
//        if(binder.isPlaying()){
//            binder.pause();
//            ivPlayStatus.setImageResource(R.mipmap.a2s);
//        }else {
//            binder.play();
//            ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
//        }
//    }

    class PlayBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "接收到消息", Toast.LENGTH_SHORT).show();
            PlayList bean = intent.getParcelableExtra(PlayListAdapter.PLAYDATA_KEY);//通过intent拿数据

            musicBinder.play(bean);
            ivPlayStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCast);//取消广播注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    //获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");//名字 类型
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void getData(String objectId) {
        OkHttpClient client = new OkHttpClient();
        String url = Constant.URL.NEWPLAYLIST + JFMusicUrlJoint.getNewPlayListUrl(objectId);
        final Request request = HttpUtils.requestGet(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                Gson gson = new Gson();
                PlayListResponse playListResponse = gson.fromJson(result, PlayListResponse.class);
                resultsBeanList.addAll(playListResponse.getResults());
                mPlayList.setObjectId(playListBean.getObjectId());
                ArrayList<PlayList.Music> musics = new ArrayList<>();
                for (int i = 0; i < playListResponse.getResults().size(); i++) {
                    PlayListResponse.ResultsBean bean = playListResponse.getResults().get(i);
                    String albumPic = bean.getAlbumPic() == null ? "" : bean.getAlbumPic().getUrl();
                    String Lrc = bean.getLrc() == null ? "" : bean.getLrc().getUrl();
                    PlayList.Music music = new PlayList.Music(
                            bean.getObjectId(),
                            bean.getTitle(),
                            bean.getArtist(),
                            bean.getFileUrl().getUrl(),
                            albumPic,
                            bean.getAlbum(),
                            Lrc
                    );
                    //使用服务的静态方法;
                    int currIndex = MusicService.getCurrIndex();
                    PlayList playList = MusicService.getCurrPlay();
                    //需要判断正在播放的歌单是否和当前界面歌单的id一致，如果一致，那么直接取正在播放的下标
                    if (currIndex != -1 && playList != null && playList.getObjectId().equals(playListBean.getObjectId())) {
                        if (i == currIndex) {
                            music.setPlayStatus(true);
                        }
                    }
                    musics.add(music);
                }
                mPlayList.setMusics(musics);
                Log.i(TAG, "onResponse: " + musics.toString());
                //在主线程刷新数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
