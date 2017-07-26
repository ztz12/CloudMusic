package com.ztz.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.utils.AppConfigUtils;

public class WelcomeActivity extends AppCompatActivity {
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏，全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(runnable,2000);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //判断是否是第一次使用
            // 使用SharedPreferences 存储是否第一次使用
            boolean guide= AppConfigUtils.getInstance().getGuide(WelcomeActivity.this);
            if(guide) {
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                finish();
            }else {
                startActivity(new Intent(WelcomeActivity.this,Main2Activity.class));
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
