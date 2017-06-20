package com.ztz.cloudmusic.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqewqe on 2017/6/15.
 */

public class DiscView extends RelativeLayout {
    private static final String TAG = "DiscView";
    List<View> viewList;
    ViewPager vpDisc;
    ImageView iv_needle;
    ImageView iv_disc;
    ImageView iv_disc_bg;
    ImageView iv_pic;
    private VPAdapter adapter;
    //    ArrayList<PlayListResponse.ResultsBean> resultBeanList;
    ArrayList<ObjectAnimator> objectAnimators = new ArrayList<>();
    /**
     * 唱针抬起角度
     */
    private float NEEDLE_UP_ROTATION;

    /**
     * 唱盘距顶部大小
     */
    private float DISC_MARGIN_TOP;

    //比例
    private static float RATIO = 0;

    private float NEEDLE_WIDTH;
    private float NEEDLE_HEIGHT;

    //唱针距离左边大小
    private float NEEDLE_MARGIN_LEFT;
    //中心点
    private float NEEDLE_PIVOT_X;
    //中心点
    private float NEEDLE_PIVOT_Y;
    //唱针距离顶部大小
    private float NEEDLE_MARGIN_TOP;

    //唱盘大小
    private float DISC_SIZE;

    //唱盘白色背景大小
    private float DISC_BG_SIZE;

    //唱盘头像大小
    private float PIC_SIZE;

    public DiscView(Context context) {
        this(context, null);
    }

    public DiscView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        initSize();
        //传入this表示加载当前布局disView
        View.inflate(context, R.layout.layout_discview, this);
        initDesc();
        initNeedle();
        initViewPager(context);
    }

    /**
     * 使用集合来储存view
     * 监听viewpager滑动事件
     *
     * @param context
     */
    private void initViewPager(Context context) {
        vpDisc = (ViewPager) findViewById(R.id.vp_disc);
//        resultBeanList=new ArrayList<>();
        viewList = new ArrayList<>();
        //模拟数据;
//        for(int i=0;i<5;i++){
//            //加载黑色底盘
//            View itemView= LayoutInflater.from(context).inflate(R.layout.item_iv_disc,vpDisc,false);
//            iv_disc_bg=(ImageView)itemView.findViewById(R.id.iv_disc_bg);
//            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) iv_disc_bg.getLayoutParams();
//            params.height= (int) DISC_SIZE;
//            params.width= (int) DISC_SIZE;
//            params.topMargin= (int) DISC_MARGIN_TOP;
//            iv_disc_bg.setLayoutParams(params);
//            iv_disc_bg.setImageResource(R.mipmap.play_disc);
//            iv_pic=(ImageView)itemView.findViewById(R.id.iv_pic);
//            RelativeLayout.LayoutParams picParams=(RelativeLayout.LayoutParams)iv_pic.getLayoutParams();
//            picParams.height= (int) PIC_SIZE;
//            picParams.width= (int) PIC_SIZE;
//            picParams.topMargin= (int)(DISC_MARGIN_TOP+(DISC_SIZE-PIC_SIZE)/2);
//            iv_pic.setLayoutParams(picParams);
//            Glide.with(context).load("http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg").into(iv_pic);
//            viewList.add(itemView);
//        }
        adapter = new VPAdapter();
        vpDisc.setAdapter(adapter);
        vpDisc.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPositionOffsetPixels = 0;//最后的位置

            //positionOffsetPixels前一个位置
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //这里的position使用和平时的常见的指示的位置不一样
                if (lastPositionOffsetPixels > positionOffsetPixels) {
//                    Log.i(TAG, "onPageScrolled: "+"右滑");
                    if (positionOffset < 0.5) {
                        if (disChange != null) {
                            disChange.onActionBarChange(mPlayList.getMusics().get(position));
                        }
                    } else {
                        disChange.onActionBarChange(mPlayList.getMusics().get(vpDisc.getCurrentItem()));
                    }
                } else if (lastPositionOffsetPixels < positionOffsetPixels) {
                    if (positionOffset > 0.5) {
                        if (disChange != null) {
                            disChange.onActionBarChange(mPlayList.getMusics().get(position + 1));
                        }
                    } else {
                        disChange.onActionBarChange(mPlayList.getMusics().get(position));
                    }
//                    Log.i(TAG, "onPageScrolled: "+"左滑");
                }
                lastPositionOffsetPixels = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                PageScrollOperation(state);
            }
        });
    }

    PlayList mPlayList;

    public void setMusicData(PlayList playList, int position) {
//        resultBeanList.clear();
//        resultBeanList.addAll(resultBean);
        mPlayList = playList;
        for (int i = 0; i < mPlayList.getMusics().size(); i++) {
            PlayList.Music bean = mPlayList.getMusics().get(i);
            //循坏歌曲列表 创建view
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_iv_disc, vpDisc, false);
            iv_disc_bg = (ImageView) view.findViewById(R.id.iv_disc_bg);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_disc_bg.getLayoutParams();
            params.height = (int) DISC_SIZE;
            params.width = (int) DISC_SIZE;
            params.topMargin = (int) DISC_MARGIN_TOP;
            iv_disc_bg.setLayoutParams(params);
            iv_disc_bg.setImageResource(R.mipmap.play_disc);
            iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            RelativeLayout.LayoutParams picParams = (RelativeLayout.LayoutParams) iv_pic.getLayoutParams();
            picParams.height = (int) PIC_SIZE;
            picParams.width = (int) PIC_SIZE;
            picParams.topMargin = (int) (DISC_MARGIN_TOP + (DISC_SIZE - PIC_SIZE) / 2);
            iv_pic.setLayoutParams(picParams);
            view.setPivotY(DISC_MARGIN_TOP + DISC_SIZE / 2);
            view.setPivotX(((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / 2);
            if (bean.getAlbumPicUrl() != null) {
                String url = bean.getAlbumPicUrl();
                Glide.with(getContext()).load(url).into(iv_pic);
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 360);
            animator.setDuration(10000);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);//重复旋转
            if (i == position) {
                animator.start();
            }
            objectAnimators.add(animator);
            viewList.add(view);
        }
        adapter.notifyDataSetChanged();
        vpDisc.setCurrentItem(position);
    }

    private void needleUp() {
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_needle, View.ROTATION, 0, NEEDLE_UP_ROTATION);
        animator2.setDuration(500);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.start();
    }

    private void needleDown() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_needle, View.ROTATION, NEEDLE_UP_ROTATION, 0);
        animator1.setDuration(500);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.start();
    }

    /**
     * 接口回调
     * 标题栏更新
     */
    public DiscChangeListener disChange;

    public interface DiscChangeListener {
        void onActionBarChange(PlayList.Music bean);
    }

    public void setDisChange(DiscChangeListener disChange) {
        this.disChange = disChange;
    }

    private void PageScrollOperation(int state) {
        switch (state) {
            /**
             * 滑动完毕和静止
             */
            case ViewPager.SCROLL_STATE_IDLE:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                needleDown();
                objectAnimators.get(vpDisc.getCurrentItem()).start();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                //滑动时
                needleUp();
                objectAnimators.get(vpDisc.getCurrentItem()).cancel();//取消动画
                break;
        }
    }

    /**
     * 播放上一首
     */
    public void playLast() {
        vpDisc.setCurrentItem(vpDisc.getCurrentItem() - 1, true);
    }

    /**
     * 播放状态
     */
    interface MusicStatus {
        int MUSIC_PLAY = 0;
        int MUSIC_PAUSE = 1;
    }

    int musicStatus = MusicStatus.MUSIC_PLAY;

    public void playPause() {
        if (musicStatus == MusicStatus.MUSIC_PLAY) {
            //正在播放 点击暂停
            needleUp();
            objectAnimators.get(vpDisc.getCurrentItem()).pause();
            musicStatus = MusicStatus.MUSIC_PAUSE;
        } else if (musicStatus == MusicStatus.MUSIC_PAUSE) {
            //暂停 点击播放
            needleDown();
            objectAnimators.get(vpDisc.getCurrentItem()).resume();
            musicStatus = MusicStatus.MUSIC_PLAY;
        }
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        vpDisc.setCurrentItem(vpDisc.getCurrentItem() + 1, true);
    }

    /**
     * 加载View大小
     */
    private void initSize() {
        RATIO = 1F * (getResources().getDisplayMetrics().heightPixels / 1920.0F);
        NEEDLE_UP_ROTATION = -30;

        /*唱针宽高、距离等比例*/
        NEEDLE_WIDTH = (float) (276.0 * RATIO);
        NEEDLE_HEIGHT = (float) (413.0 * RATIO);

        NEEDLE_MARGIN_LEFT = (float) (550.0 * RATIO);
        NEEDLE_PIVOT_X = (float) (43.0 * RATIO);
        NEEDLE_PIVOT_Y = (float) (43.0 * RATIO);
        NEEDLE_MARGIN_TOP = (float) (43.0 * RATIO);

        /*唱盘比例*/
        DISC_SIZE = (float) (804.0 * RATIO);
        DISC_BG_SIZE = (float) (810.0 * RATIO);

        DISC_MARGIN_TOP = (190 * RATIO);

        /*专辑图片比例*/
        PIC_SIZE = (float) (533.0 * RATIO);


    }


    /**
     * 加载唱针
     */
    private void initNeedle() {
        iv_needle = (ImageView) findViewById(R.id.iv_needle);
        RelativeLayout.LayoutParams params = (LayoutParams) iv_needle.getLayoutParams();
        params.width = (int) NEEDLE_WIDTH;
        params.height = (int) NEEDLE_HEIGHT;
        params.leftMargin = (int) NEEDLE_MARGIN_LEFT;
        params.topMargin = (int) NEEDLE_MARGIN_TOP * -1;
        iv_needle.setLayoutParams(params);
        iv_needle.setPivotX(NEEDLE_PIVOT_X);
        iv_needle.setPivotY(NEEDLE_PIVOT_Y);
        //唱针抬起
//        iv_needle.setRotation(NEEDLE_UP_ROTATION);
    }

    /**
     * 加载白色底盘
     */
    private void initDesc() {
        iv_disc = (ImageView) findViewById(R.id.iv_disc_view);
        RelativeLayout.LayoutParams params = (LayoutParams) iv_disc.getLayoutParams();
        params.width = (int) DISC_BG_SIZE;
        params.height = (int) DISC_BG_SIZE;
        params.topMargin = (int) DISC_MARGIN_TOP;
        iv_disc.setLayoutParams(params);
    }

    class VPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
}
