package com.ztz.cloudmusic.login.fragment;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ztz.cloudmusic.utils.AppStringUtils;
import com.ztz.cloudmusic.activity.Main2Activity;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.LoginResponse;
import com.ztz.cloudmusic.utils.AppConfigUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wqewqe on 2017/6/5.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;
    @BindView(R.id.bt_login)
    AppCompatButton btLogin;
    Unbinder unbinder;
    private static final String TAG = "LoginFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_register, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                RegisterFragment fragment=new RegisterFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_content,fragment);
                //将fragment添加到返回栈中
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bt_login:
                String name=etName.getText().toString();
                String password=etPassword.getText().toString();
                if(!AppStringUtils.checkUserName(name)){
                    etName.setError("用户名格式不正确");
                }
                if(!AppStringUtils.checkPassword(password)){
                    etPassword.setError("密码格式不正确");
                }
                login(name,password);
                break;
        }
    }

    private void login(String name, String password) {
        showProgressDialog("登录中...");
        OkHttpClient client=new OkHttpClient();
        String url="https://leancloud.cn:443/1.1/login?username=" + name + "&password=" + password;

        //测试账号  aaa  123456
        //测试账号  weidong  123456
        //测试账号  bbb  123456
        //测试账号  ccc  123456
        //测试账号  ddd  123456
        Request request=new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i(TAG, "登录失败");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String resultData=response.body().string();
                Log.i(TAG, "onResponse: "+resultData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            if(response.code()==200){//获取请求结果码
                                //解析成功的数据
                                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                                Gson gson=new Gson();
                                LoginResponse loginResponse=gson.fromJson(resultData,LoginResponse.class);
                                Log.i(TAG, "run: "+loginResponse.toString());
                                Intent intent=new Intent(getActivity(), Main2Activity.class);
                                startActivity(intent);
                                AppConfigUtils.getInstance().setGuide(getContext(),false);
                            }else {
                                //{"code":211,"error":"Could not find user"} 用户不存在
                                //{"code":210,"error":"The username and password mismatch."} 用户名和密码不匹配
                                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                            }
                            closeProgressDialog();
                    }
                });
            }
        });

    }
}
