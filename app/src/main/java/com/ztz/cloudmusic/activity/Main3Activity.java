package com.ztz.cloudmusic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Url;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

    }
    public void click(View view){
      Url.showToast(this,"你好");
//        Toast.makeText(this, "你好", Toast.LENGTH_SHORT).show();
    }
}
