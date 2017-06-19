package com.ztz.cloudmusic.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.login.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        LoginFragment fragment=new LoginFragment();
        transaction.add(R.id.fl_content,fragment);
        transaction.commit();
    }
}
