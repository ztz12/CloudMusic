package com.ztz.cloudmusic.activity.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.PlayList;
import com.ztz.cloudmusic.service.MusicService;

/**
 * Created by wqewqe on 2017/6/16.
 */

//public abstract class BaseActivity extends AppCompatActivity {
public class BaseActivity extends AppCompatActivity {
    /**
     * app:layout_behavior="@string/bottom_sheet_behavior"
     * 这个属性需要设置在触发滚动事件的view之上
     * app:behavior_hideable="false"说明这个BottomSheet不可以被手动滑动隐藏，设置为true则可以滑到屏幕最底部隐藏。
     * app:behavior_peekHeight设置的是折叠状态时的高度。
     * 需要监听Bottom Sheets回调的状态，可以通过setBottomSheetCallback来实现,可以通过
     * BottomSheetBehavior.setState 方法来设置开关状态
     * STATE_COLLAPSED: 关闭Bottom Sheets,显示peekHeight的高度，默认是0
     * STATE_DRAGGING: 用户拖拽Bottom Sheets时的状态
     * STATE_SETTLING: 当Bottom Sheets view摆放时的状态。
     * STATE_EXPANDED: 当Bottom Sheets 展开的状态
     * STATE_HIDDEN: 当Bottom Sheets 隐藏的状态
     *
     * @param savedInstanceState
     */
    public static String TAG = "BaseActivity";
    public ImageView iv_playStatus;
    public MusicBroadCastReceiver musicReceiver;
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

    private RecyclerView rlView;
    public MusicService.MusicBinder musicBinder;
    private PlayListAdapter adapter;


    //方式二
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //加载父类布局
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        ImageButton ib=(ImageButton)view.findViewById(R.id.ib);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBehavior();
            }
        });
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

    public void hideBehavior(){
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        rlView = (RecyclerView) bottomView.findViewById(R.id.rl_base);
        ImageView ivShow = (ImageView) bottomView.findViewById(R.id.iv_show);
        LinearLayout ll = (LinearLayout) bottomView.findViewById(R.id.ll_base);
        rlView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayListAdapter(playList);
        rlView.setAdapter(adapter);
        behavior = BottomSheetBehavior.from(ll);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if(newState==BottomSheetBehavior.STATE_EXPANDED){
//                    findViewById(R.id.ll_content).setAlpha(0.5F);
//                }else {
//                    findViewById(R.id.ll_content).setAlpha(1);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取正在播放的歌单
                //使用临时变量获取数据
                PlayList currPlayList=MusicService.getCurrPlay();
                //播放列表为空
                if(currPlayList==null){
                    Toast.makeText(BaseActivity.this, "播放列表为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * RecyclerView使用的是观察者模式
                 * 观察者模式会一直观察这个对象，当对象数据发生改变是会自动/被动做一些操作
                 */
                //不改变对象引用的地址更新数据
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                playList.setMusics(currPlayList.getMusics());
                playList.setObjectId(currPlayList.getObjectId());
                adapter.notifyDataSetChanged();
            }
        });
    }

    BottomSheetBehavior behavior;
    PlayList playList = new PlayList();

    class PlayListAdapter extends RecyclerView.Adapter {
        PlayList playList;

        public PlayListAdapter(PlayList playList) {
            this.playList = playList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_playBase;
            TextView tv_playBase;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_playBase = (ImageView) itemView.findViewById(R.id.iv_basePlay);
                tv_playBase = (TextView) itemView.findViewById(R.id.tv_base_name);
            }
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final PlayList.Music bean = playList.getMusics().get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            if (bean.isPlayStatus()) {
                viewHolder.iv_playBase.setVisibility(View.VISIBLE);
                viewHolder.tv_playBase.setTextColor(getResources().getColor(R.color.color4));
            } else {
                viewHolder.iv_playBase.setVisibility(View.GONE);
                viewHolder.tv_playBase.setTextColor(getResources().getColor(R.color.color6));
            }
            viewHolder.tv_playBase.setText(bean.getAlbum() + "-" + bean.getArtist());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i=0;i<playList.getMusics().size();i++){
                        playList.getMusics().get(i).setPlayStatus(false);
                    }

                    //获取点击的歌曲
                    bean.setPlayStatus(true);
                    notifyDataSetChanged();
                    long start=System.currentTimeMillis();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            musicBinder.play(playList);
                        }
                    }).start();
                    long end=System.currentTimeMillis();
                    Log.i(TAG, "onClick:执行时间 "+(end-start));
                }
            });
        }

        @Override
        public int getItemCount() {
            return playList.getMusics().size();
        }
    }

    /**
     * 通过Activity生命周期去控制
     * 当Activity可见时，进行播放状态判断
     */

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(musicBinder!=null) {
//            if (musicBinder.isPlaying()) {
//                iv_playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
//            } else {
//                iv_playStatus.setImageResource(R.mipmap.a2s);
//            }
//        }
//    }
    private void register() {
        musicReceiver = new MusicBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //注册多个广播
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
                    musicStatusChange();
                    break;
                case Constant.Action.PAUSE:
                    iv_playStatus.setImageResource(R.mipmap.a2s);
            }
        }
    }

    /**
     * 音乐状态改变 播放新音乐
     */
    public void musicStatusChange(){}

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            //进入到界面 判断当前播放状态
            if (musicBinder.isPlaying()) {
                iv_playStatus.setImageResource(R.mipmap.play_rdi_btn_pause);
            } else {
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
