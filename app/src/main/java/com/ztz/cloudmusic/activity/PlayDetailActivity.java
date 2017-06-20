package com.ztz.cloudmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.PlayList;
import com.ztz.cloudmusic.widget.DiscView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

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
//    private PlayListResponse.ResultsBean resultsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_detail);
        ButterKnife.bind(this);
        mPlayList = getIntent().getParcelableExtra(RESULTBEAN_KEY);
        Log.i(TAG, "onCreate: " + mPlayList);
//        resultsBean = getIntent().getParcelableExtra(DETAIL_KEY);
//        Log.i(TAG, "onCreate: " + resultsBean);
        //获取下标
        int position = getIntent().getIntExtra(INDEX_KEY, 0);
        PlayList.Music music = mPlayList.getMusics().get(position);
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
        });
        disv.setMusicData(mPlayList, position);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.iv_last, R.id.iv_now, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                disv.playLast();
                break;
            case R.id.iv_now:
                ivNow.setSelected(!ivNow.isSelected());
                if (ivNow.isSelected()) {
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_play);
                } else {
                    ivNow.setImageResource(R.mipmap.play_rdi_btn_pause);
                }
                disv.playPause();
                break;
            case R.id.iv_next:
                disv.playNext();
                break;
        }
    }
}
