package com.ztz.cloudmusic.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ztz.cloudmusic.R;

public class NewFriendActivity extends BaseActivity {
//    @Override
//    public int layoutResID() {
//        return R.layout.activity_new_friend;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
    }
    public void test(View view){
        startActivity(new Intent(this,TestActivity.class));
    }
}
