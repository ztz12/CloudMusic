package com.ztz.cloudmusic.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ztz.cloudmusic.utils.AppStringUtils;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by wqewqe on 2017/6/5.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.register)
    public void onViewClicked() {
        showProgressDialog("请等待...");
        String username = etName.getText().toString();
        String password = etPassword.getText().toString();
        if (!AppStringUtils.checkUserName(username)) {
            etName.setError("请输入正确用户名格式");
        }
        if (!AppStringUtils.checkPassword(password)) {
            etPassword.setError("请输入正确密码格式");
        }
        register(username, password);
    }

    private void register(String userName, String password) {
        OkHttpClient client = new OkHttpClient();
        //将两个参数封装成json对象
        User user = new User(userName, password);
        Gson gson = new Gson();
        String json = gson.toJson(user);//将user转换为json数据格式
        Log.i(TAG, "register: " + json);
        //告诉服务器，我们上传的格式是json格式
        MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSONTYPE, json);
        String url = "https://leancloud.cn:443/1.1/users";
        Request request = new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 201) {
                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String sessionToken = jsonObject.getString("sessionToken");
                                String updatedAt = jsonObject.getString("updatedAt");
                                String objectId = jsonObject.getString("objectId");
                                String username = jsonObject.getString("username");
                                String createdAt = jsonObject.getString("createdAt");
                                boolean emailVerified = jsonObject.getBoolean("emailVerified");
                                boolean mobilePhoneVerified = jsonObject.getBoolean("mobilePhoneVerified");
                                Log.e(TAG, "run: " + sessionToken +
                                        updatedAt + username
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }
                });
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void backClick() {
        //返回上一个fragment
        getFragmentManager().popBackStack();
    }
}
