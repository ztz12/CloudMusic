package com.ztz.cloudmusic.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.rd.PageIndicatorView;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.utils.AppConfigUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {
    ViewPager vp;
    List<View> views = new ArrayList<>();
    MyAdapter adapter;
    private static final String TAG = "GuideActivity";
    private float ratio;
    private int currentIndex;
    String[] titles = new String[]{
            "个 性 推 荐",
            "精 彩 评 论",
            "海 量 资 讯"
    };
    String[] desc = new String[]{
            "每 天 为 你 量 身 推 荐 最 合 口 味 的 好 音 乐",
            "4 亿 多 条 有 趣 的 故 事，听 歌 再 不 孤 单",
            "明 星 动 态、音 乐 热 点 尽 收 眼 底"
    };
    TextSwitcher titleSwitchs;
    TextSwitcher descSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        vp = (ViewPager) findViewById(R.id.vp);
        initViews();
    }
    public void btn_experience(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        //存储第一次数据 修改为false
        AppConfigUtils.getInstance().setGuide(this,false);
    }
    private void initViews() {
        //获取图片资源和屏幕的比例
        ratio = 1F * (getResources().getDisplayMetrics().heightPixels / 1920.0F);
        titleSwitchs = (TextSwitcher) findViewById(R.id.ts_title);
        titleSwitchs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(GuideActivity.this);
                tv.setTextSize(24);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(Typeface.DEFAULT_BOLD);//字体加粗
                return tv;
            }
        });
        descSwitch = (TextSwitcher) findViewById(R.id.ts_desc);
        descSwitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(GuideActivity.this);
                tv.setTextSize(13);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                return tv;
            }
        });
        titleSwitchs.setText(titles[0]);
        descSwitch.setText(desc[0]);
        final View view1 = LayoutInflater.from(this).inflate(R.layout.layout_guide1, null);
        setViewSize(view1, 0);
        View view2 = LayoutInflater.from(this).inflate(R.layout.layout_guide2, null);
        setViewSize(view2, 1);
        View view3 = LayoutInflater.from(this).inflate(R.layout.layout_guide3, null);
        setViewSize(view3, 2);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        adapter = new MyAdapter();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = views.get(position);
                if (position == 1) {
                    ivAesAnimator(view.findViewById(R.id.iv_aes), (517F * ratio));
                    ivAekAnimator(view.findViewById(R.id.iv_aet));
                    ivAekAnimator(view.findViewById(R.id.iv_aek));
                }
                if (position == 2) {
                    ivAesAnimator(view.findViewById(R.id.iv_aet), (333F * ratio));
                    ivAekAnimator(view.findViewById(R.id.iv_aek));
                }

                if (currentIndex > position) {
                    titleSwitchs.setInAnimation(GuideActivity.this, R.anim.left_in);
                    titleSwitchs.setOutAnimation(GuideActivity.this, R.anim.left_out);
                    descSwitch.setInAnimation(GuideActivity.this, R.anim.left_in);
                    descSwitch.setOutAnimation(GuideActivity.this, R.anim.left_out);
                } else {
                    titleSwitchs.setInAnimation(GuideActivity.this, R.anim.right_in);
                    titleSwitchs.setOutAnimation(GuideActivity.this, R.anim.right_out);
                    descSwitch.setInAnimation(GuideActivity.this, R.anim.right_in);
                    descSwitch.setOutAnimation(GuideActivity.this, R.anim.right_out);
                }
                titleSwitchs.setText(titles[position]);
                descSwitch.setText(desc[position]);
                currentIndex = position;
            }

            private void ivAesAnimator(View view, float size) {
                /**
                 * ValueAnimator 只不过是对值进行了一个平滑的动画过渡
                 * ObjectAnimator 可以直接对任意对象的任意属性进行动画操作
                 */
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", size, 0);
                animator.setDuration(800);
                animator.start();

            }

            public void ivAekAnimator(View view) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{0F, 1F});
                animator.setDuration(1000);
                animator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp.setPageTransformer(true, new ViewPager.PageTransformer() {
            /**
             *
             * @param page 当前滑动view
             * @param position View 所占屏幕空间
             *                 值为0表示在屏幕中间
             *                 值为-1表示滑出屏幕
             */
            @Override
            public void transformPage(View page, float position) {
                Log.i(TAG, "transformPage: " + page.getClass() + " " + position);
                int pageWidth = page.getWidth();
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
                    if (position < 0) {
                        page.setTranslationX(-pageWidth * position);
                    } else {

                        page.setTranslationX(pageWidth);
                        //抵消滑动效果
                        page.setTranslationX(-pageWidth * position);
                    }
                    page.setAlpha(Math.max(0, 1 - Math.abs(position)));
                } else {
                    page.setAlpha(0);
                }
            }
        });
        PageIndicatorView pageView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageView.setViewPager(vp);
        pageView.setRadius(15F * ratio);
        pageView.setPadding((int) (5 * ratio), 0, (int) (5 * ratio), 0);
        View bg_red = findViewById(R.id.bg_red);
        bg_red.getLayoutParams().height = (int) (1185 * ratio);
    }


    private void setViewSize(View view, int index) {
        //设置中间view大小
        view.findViewById(R.id.cv_content).getLayoutParams().width = (int) (803F * ratio);
        view.findViewById(R.id.cv_content).getLayoutParams().height = (int) (1218F * ratio);
        int ivAesSize = (int) (237F * ratio);//获取图片宽高
        int marginLeft = (int) (40F * ratio);
        if (index == 0) {
            ImageView iv_aes = (ImageView) view.findViewById(R.id.iv_aes);
            FrameLayout.LayoutParams ivAesParams = (FrameLayout.LayoutParams) iv_aes.getLayoutParams();
            int marginTop = (int) (552F * ratio);
            ivAesParams.height = ivAesSize;
            ivAesParams.width = ivAesSize;
            ivAesParams.topMargin = marginTop;
            ivAesParams.leftMargin = marginLeft;
        }
        if (index == 1) {
            FrameLayout.LayoutParams view2_iv_aesParams = (FrameLayout.LayoutParams) view.findViewById(R.id.iv_aes).getLayoutParams();
            view2_iv_aesParams.height = ivAesSize;
            view2_iv_aesParams.width = ivAesSize;
            view2_iv_aesParams.topMargin = marginLeft;
            view2_iv_aesParams.leftMargin = marginLeft;
            ImageView iv_aet = (ImageView) view.findViewById(R.id.iv_aet);
            FrameLayout.LayoutParams iv_aetParams = (FrameLayout.LayoutParams) iv_aet.getLayoutParams();
            iv_aetParams.topMargin = (int) (333F * ratio);
            iv_aetParams.leftMargin = marginLeft;
        }
        if (index == 2) {
            ImageView iv_aet = (ImageView) view.findViewById(R.id.iv_aet);
            FrameLayout.LayoutParams iv_aetParams = (FrameLayout.LayoutParams) iv_aet.getLayoutParams();
            iv_aetParams.leftMargin = marginLeft;
            iv_aetParams.topMargin = marginLeft;
        }

    }

    @OnClick(R.id.btn_login)
    public void btn_login(View view) {
        Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
        startActivity(intent);

    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
