package com.ztz.cloudmusic.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.MusicResponse;

import java.util.List;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class PlayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEAD=0;
    private static final int TYPE_NOR=1;
    List<MusicResponse.ResultsBean> resultsBeanList;

    public PlayAdapter(List<MusicResponse.ResultsBean> resultsBeanList) {
        this.resultsBeanList = resultsBeanList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int temp=getItemViewType(position);
                    if(headView!=null&&temp==TYPE_HEAD){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEAD) {
            return new HeadViewHolder(headView);//要返回return
        }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play, parent, false);
            PlayViewHolder holder = new PlayViewHolder(view);
            return holder;

    }
    View headView;
    public void setHead(View view){
        headView=view;
    }
    class HeadViewHolder extends RecyclerView.ViewHolder{

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(headView!=null&&position==0){
            return TYPE_HEAD;
        }
        return TYPE_NOR;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(headView!=null&&position==0){
            return;
        }
        if(headView!=null){
            position=position-1;
        }
        MusicResponse.ResultsBean resultsBean=resultsBeanList.get(position);
        PlayViewHolder playViewHolder= (PlayViewHolder) holder;
        try {
            playViewHolder.smartImageView.setImageUrl(resultsBean.getPicUrl().getUrl());
        }catch (Exception e){}

        playViewHolder.tvPlay.setText(resultsBean.getName());
    }
    class PlayViewHolder extends RecyclerView.ViewHolder{
        SmartImageView smartImageView;
        TextView tvPlay;
        public PlayViewHolder(View itemView) {
            super(itemView);
            smartImageView=(SmartImageView)itemView.findViewById(R.id.smart_playlist);
            tvPlay=(TextView)itemView.findViewById(R.id.tv_listName);
        }
    }
    @Override
    public int getItemCount() {
        return resultsBeanList.size()+(headView!=null?1:0);
    }
}
