package com.ztz.cloudmusic.fragment.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.adapter.PlayAdapter;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.MusicResponse;
import com.ztz.cloudmusic.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class PlayListFragment extends Fragment{
    RecyclerView rl_list;
    private static final String TAG = "PlayListFragment";
    List<MusicResponse.ResultsBean> beanList=new ArrayList<>();
    PlayAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlist,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_list=(RecyclerView)view.findViewById(R.id.rec_playlist);

        rl_list.setLayoutManager(new GridLayoutManager(getContext(),2));
        View headView=LayoutInflater.from(getContext()).inflate(R.layout.layout_playlist_head,rl_list,false);
        adapter=new PlayAdapter(beanList);
        adapter.setHead(headView);
        rl_list.setAdapter(adapter);
        getData();
    }

    private void getData() {
        String url= Constant.URL.PLAYLIST;
        OkHttpClient client=new OkHttpClient();
        HttpUrl.Builder builder=HttpUrl.parse(url).newBuilder();
        builder.addQueryParameter("limit","20");
        Request request= HttpUtils.requestGet(builder.build());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.i(TAG, "onResponse: "+result);
                Gson gson=new Gson();
                MusicResponse musicResponse=gson.fromJson(result,MusicResponse.class);
                try{
                    beanList.addAll(musicResponse.getResults());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }


}
