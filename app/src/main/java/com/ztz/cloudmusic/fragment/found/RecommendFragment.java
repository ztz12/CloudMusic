package com.ztz.cloudmusic.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.activity.AdjustColumnActivity;
import com.ztz.cloudmusic.adapter.BannerAdapter;
import com.ztz.cloudmusic.adapter.HomeAdapter;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.Home;
import com.ztz.cloudmusic.bean.HomeResponse;
import com.ztz.cloudmusic.bean.Result;
import com.ztz.cloudmusic.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class RecommendFragment extends Fragment {
    private static final String TAG = "RecommendFragment";
    ViewPager vp;
    BannerAdapter adapter;
    private SmartImageView smartView;
    ArrayList<Home> homes = new ArrayList<>();
    HomeAdapter homeAdapter;
    RecyclerView rl_home;
    List<Result> resultList = new ArrayList<>();
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        run();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_home = (RecyclerView) view.findViewById(R.id.rl_home);
        rl_home.setLayoutManager(new LinearLayoutManager(getContext()));
        homeAdapter = new HomeAdapter(homes);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_header, null);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_footer, null);
        vp = (ViewPager) headerView.findViewById(R.id.vp_main2);
        homeAdapter.setHeaderView(headerView);
        homeAdapter.setFooterView(footerView);
        rl_home.setAdapter(homeAdapter);
        adapter = new BannerAdapter(resultList);
        homeAdapter.setItemSelector(recommendedItemSelector);
        getDate();
        getHome();
        Button btn = (Button) footerView.findViewById(R.id.btn_footer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdjustColumnActivity.class);
                intent.putParcelableArrayListExtra("homes", homes);
                startActivityForResult(intent, 1);//请求码为1；
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i(TAG, "onPageScrollStateChanged:ViewPager滚动 "+state);
                switch (state) {
                    case 1://滑动时让轮播图停止
                        stop();
                        break;
                    case 2://滑动完毕时让轮播图继续播放
                        if (scheduledExecutorService == null) {
                            run();
                        }
                        break;
                }
            }
        });
    }

    HomeAdapter.RecommendedItemSelector recommendedItemSelector;

    public void setItemSelector(HomeAdapter.RecommendedItemSelector itemSelector) {
//        homeAdapter.setItemSelector(itemSelector);
        recommendedItemSelector = itemSelector;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            List<Home> result = data.getParcelableArrayListExtra("homes");
            homes.clear();
            homes.addAll(result);
            homeAdapter.notifyDataSetChanged();
            rl_home.smoothScrollToPosition(0);
        }
    }

    private void getHome() {
        String url = Constant.URL.HOME + "?include=playList%2CplayList.author&";
        OkHttpClient client = new OkHttpClient();
        Request request = HttpUtils.requestGet(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                Gson gson = new Gson();
                HomeResponse homeResponse = gson.fromJson(result, HomeResponse.class);
                Log.i(TAG, "onResponse: " + homeResponse);
                //无序
                HashMap<String, ArrayList<HomeResponse.ResultsBean.PlayListBean>> hashMap = new HashMap<>();
                for (int i = 0; i < homeResponse.getResults().size(); i++) {
                    HomeResponse.ResultsBean resultsBean = homeResponse.getResults().get(i);
                    HomeResponse.ResultsBean.PlayListBean playListBean = homeResponse.getResults().get(i).getPlayList();
                    //获取集合中所有数据
//                    Home home=new Home();
//                    home.setName(homeResponse.getResults().get(i).getItem());
//                    home.setType(homeResponse.getResults().get(i).getType());
//                    home.getPlayListBeen().add(playListBean);
//                    homes.add(home);
                    String item = resultsBean.getItem();
                    if (hashMap.containsKey(item)) {
                        //包含
                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultBeen = hashMap.get(item);
                        resultBeen.add(playListBean);
                    } else {
                        //不包含 Home.ResultsBean.PlayListBean 使用类中的内部类中的内部类
                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultBeen = new ArrayList<>();
                        resultBeen.add(playListBean);
                        hashMap.put(item, resultBeen);
                    }
                }
                Log.i(TAG, "onResponse: " + hashMap);
                Set<Map.Entry<String, ArrayList<HomeResponse.ResultsBean.PlayListBean>>> entrySet = hashMap.entrySet();
                for (Map.Entry<String, ArrayList<HomeResponse.ResultsBean.PlayListBean>> entry : entrySet) {
                    String name = entry.getKey();
                    ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen = entry.getValue();
                    Home home = new Home();
                    home.setName(name);
                    home.setPlayListBeen(playListBeen);
                    homes.add(home);
                }
                Log.i(TAG, "onResponse: " + homes);
            }
        });
    }

    int currentItem = 0;

    public void run() {
        //开启线程池实现轮播图效果
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
    }

    public class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % resultList.size();
            handle.sendEmptyMessage(0);
        }
    }

    public Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            vp.setCurrentItem(currentItem);
        }
    };

    public void stop() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    private void getDate() {
        String url = "https://leancloud.cn:443/1.1/classes/Banner?limit=6";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String picurl = object.getString("picurl");
                        String desc = object.getString("desc");
                        String createdAt = object.getString("createdAt");
                        String updatedAt = object.getString("updatedAt");
                        String objectId = object.getString("objectId");
                        Result res = new Result(picurl, desc, createdAt, updatedAt, objectId);
                        smartView = new SmartImageView(getContext());
                        //注意：使用xml的布局可以直接使用 imageView.getLayoutParams()
                        //如果是通过代码new出来的View，不能使用该方法，必须主动创建LayoutParams对象
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        //设置默认参数
                        smartView.setLayoutParams(params);
                        //设置图片
                        res.setSmartImageView(smartView);


                        resultList.add(res);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vp.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
