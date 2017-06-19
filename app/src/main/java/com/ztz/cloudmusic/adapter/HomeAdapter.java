package com.ztz.cloudmusic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.activity.PlayListActivity;
import com.ztz.cloudmusic.bean.Home;
import com.ztz.cloudmusic.bean.HomeResponse;

import java.util.ArrayList;

/**
 * Created by wqewqe on 2017/6/9.
 */

public class HomeAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;
    ArrayList<Home> homesList;
    private View headerView;
    private View footerView;

    public HomeAdapter(ArrayList<Home> homesList) {
        this.homesList = homesList;
    }

    public void setHeaderView(View view) {
        headerView = view;
    }

    public void setFooterView(View view) {
        footerView = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(headerView);
        }
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(footerView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断当前布局是否是头布局,是直接return
        if (headerView != null && position == 0) {
            return;
        }
        if (footerView != null && position == getItemCount() - 1) {
            return;
        }
        if (headerView != null) {
            position = position - 1;
        }
        ((ItemViewHolder) holder).tv_name.setText(homesList.get(position).getName());
        //新专辑上架   3
        //最新音乐    2
        //推荐歌单    1
        int spanCount = 0;
        if (homesList.get(position).getName().equals("新专辑上架")) {
            spanCount = 3;
        } else if (homesList.get(position).getName().equals("最新音乐")) {
            spanCount = 2;
        } else if (homesList.get(position).getName().equals("推荐歌单")) {
            spanCount = 1;
        }
        MyAdapter adapter = new MyAdapter(homesList.get(position).getPlayListBeen());
        ((ItemViewHolder) holder).rl.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), spanCount));
        ((ItemViewHolder) holder).rl.setAdapter(adapter);
        ((ItemViewHolder) holder).tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemSelector!=null){
                    itemSelector.selector();
                }
            }
        });
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        RecyclerView rl;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            rl = (RecyclerView) itemView.findViewById(R.id.rl);
        }
    }

    @Override
    public int getItemCount() {
        return homesList.size() + (headerView != null ? 1 : 0) + (footerView != null ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (footerView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

  class MyAdapter extends RecyclerView.Adapter {
        ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen;

        public MyAdapter(ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen) {
            this.playListBeen = playListBeen;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_child, parent, false);
            ChildViewHolder holder = new ChildViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            final HomeResponse.ResultsBean.PlayListBean playListBean = playListBeen.get(position);
            childViewHolder.sI.setImageUrl(playListBean.getPicUrl());
            try {
                childViewHolder.tv_name.setText(playListBean.getAuthor().getUsername());
            } catch (Exception e) {
            }
            childViewHolder.tv_album.setText(playListBean.getPlayListName());
            childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), PlayListActivity.class);
                    intent.putExtra(PlayListActivity.PLAYLISTBEAN_KEY, playListBean);
                    try {
                        intent.putExtra(PlayListActivity.AUTHOR_KEY, playListBean.getAuthor().getUsername());
                    } catch (Exception e) {
                    }
//                    childViewHolder.itemView.getContext().startActivity(intent);
                    ((Activity) holder.itemView.getContext()).startActivity(intent);
                }
            });

        }

        class ChildViewHolder extends RecyclerView.ViewHolder {
            SmartImageView sI;
            TextView tv_name;
            TextView tv_album;

            public ChildViewHolder(View itemView) {
                super(itemView);
                sI = (SmartImageView) itemView.findViewById(R.id.smart_view);
                tv_name = (TextView) itemView.findViewById(R.id.tv_child_name);
                tv_album = (TextView) itemView.findViewById(R.id.tv_album);
            }
        }

        @Override
        public int getItemCount() {
            return playListBeen.size();
        }

    }
    private RecommendedItemSelector itemSelector;
    public void setItemSelector(RecommendedItemSelector itemSelector){
        this.itemSelector=itemSelector;
    }

    /**
     * 1.接口的访问修饰符
     接口的访问修饰符可以是public 或者是默认修饰符default，只能是这两种
     如果接口本身被定义为public ，所有方法和变量都是public 的
     */
    public interface RecommendedItemSelector{
        void selector();
    }
}
