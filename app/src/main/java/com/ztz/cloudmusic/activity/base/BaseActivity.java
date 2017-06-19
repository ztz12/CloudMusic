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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.service.MusicService;

import java.util.ArrayList;

/**
 *
 * Created by wqewqe on 2017/6/16.
 */

//public abstract class BaseActivity extends AppCompatActivity {
public class BaseActivity extends AppCompatActivity {
    /**
     * app:layout_behavior="@string/bottom_sheet_behavior"
     这个属性需要设置在触发滚动事件的view之上
     app:behavior_hideable="false"说明这个BottomSheet不可以被手动滑动隐藏，设置为true则可以滑到屏幕最底部隐藏。
     app:behavior_peekHeight设置的是折叠状态时的高度。
     需要监听Bottom Sheets回调的状态，可以通过setBottomSheetCallback来实现,可以通过
     BottomSheetBehavior.setState 方法来设置开关状态
     STATE_COLLAPSED: 关闭Bottom Sheets,显示peekHeight的高度，默认是0
     STATE_DRAGGING: 用户拖拽Bottom Sheets时的状态
     STATE_SETTLING: 当Bottom Sheets view摆放时的状态。
     STATE_EXPANDED: 当Bottom Sheets 展开的状态
     STATE_HIDDEN: 当Bottom Sheets 隐藏的状态
     * @param savedInstanceState
     */
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
     ArrayList<String> stringList=new ArrayList<>();
    private RecyclerView rlView;

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
        rlView = (RecyclerView)bottomView.findViewById(R.id.rl_base);
        ImageView ivShow=(ImageView)bottomView.findViewById(R.id.iv_show);
        LinearLayout ll=(LinearLayout)bottomView.findViewById(R.id.ll_base);
        rlView.setLayoutManager(new LinearLayoutManager(this));
        for(int i=0;i<20;i++){
            stringList.add("Item: "+i);
        }
        PlayListAdapter adapter=new PlayListAdapter(stringList);
        rlView.setAdapter(adapter);
        behavior=BottomSheetBehavior.from(ll);
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }
    BottomSheetBehavior behavior;
    class PlayListAdapter extends RecyclerView.Adapter{
        ArrayList<String> strings;

        public PlayListAdapter(ArrayList<String> strings) {
            this.strings = strings;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(strings.get(position) +" ");
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }
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
