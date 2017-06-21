package com.ztz.cloudmusic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.activity.PlayDetailActivity;
import com.ztz.cloudmusic.bean.Constant;
import com.ztz.cloudmusic.bean.PlayList;

/**
 * Created by wqewqe on 2017/6/9.
 */

public class PlayListAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER=0;
    public static final int TYPE_FOOTER=1;
    public static final int TYPE_NORMAL=2;
    public static final String PLAYDATA_KEY="playData";
    PlayList playList;

    private View headerView;
    private View footerView;
    public PlayListAdapter(PlayList playList) {
        this.playList = playList;
    }
    public void setHeaderView(View view){
        headerView=view;
    }
    public void setFooterView(View view){
        footerView=view;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            return new HeaderViewHolder(headerView);
        }
        if(viewType==TYPE_FOOTER){
            return new FooterViewHolder(footerView);
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        ItemViewHolder itemViewHolder=new ItemViewHolder(view);
        return itemViewHolder;
    }
    ////记录上一次操作的bean，再次点击，修改该bean播放状态，并且重写赋值
    PlayList.Music mLastPosition;
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //判断当前布局是否是头布局,是直接return
        if(headerView!=null&&position==0){
            return;
        }
        if(footerView!=null&&position==getItemCount()-1){
            return;
        }
        if(headerView!=null){
            position=position-1;
        }

        final PlayList.Music bean=playList.getMusics().get(position);
        final ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
        if(bean.isPlayStatus()){
            itemViewHolder.iv_playStatus.setVisibility(View.VISIBLE);
            itemViewHolder.tv_number.setVisibility(View.INVISIBLE);
        }else {
            itemViewHolder.iv_playStatus.setVisibility(View.INVISIBLE);
            itemViewHolder.tv_number.setVisibility(View.VISIBLE);
            itemViewHolder.tv_number.setText((position+1)+"");
        }

        itemViewHolder.tv_name.setText(bean.getAlbum()+"-"+bean.getArtist());
        itemViewHolder.tv_album.setText(bean.getTitle());
        final int finalPosition = position;
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               String url=bean.getFileUrl().getUrl();

//                for(int i=0;i<resultsBeanList.size();i++){
//                    resultsBeanList.get(i).setPlayStatus(false);
//                }
                //判断当前条目和最后一个条目是否相等
//                if(bean.equals(mLastPosition)){
                if (bean.isPlayStatus()) {
                    Intent intent = new Intent(holder.itemView.getContext(), PlayDetailActivity.class);
                    intent.putExtra(PlayDetailActivity.DETAIL_KEY, bean);
                    /**
                     *  Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value
                     *  上限下限为ArrayList必须用ArrayList
                     *
                     *因为在播放详情界面，需要拿到所有的歌曲列表，所以需要将集合传递给PlayDetailsActivity
                     */
                    intent.putExtra(PlayDetailActivity.RESULTBEAN_KEY, playList);
                    intent.putExtra(PlayDetailActivity.INDEX_KEY, finalPosition);
                    ((Activity) holder.itemView.getContext()).startActivity(intent);
                } else {
//                if(mLastPosition!=null){
//                    mLastPosition.setPlayStatus(false);
//                }
                    //重置所有状态
                    for (int i = 0; i < playList.getMusics().size(); i++) {
                        playList.getMusics().get(i).setPlayStatus(false);
                    }

                    bean.setPlayStatus(true);
                    notifyDataSetChanged();
//                mLastPosition=bean;//重新赋值
                    //本地广播
                    Intent intent = new Intent(Constant.Action.ACTION_PLAY);
                    //实现parcelable序列化
                    intent.putExtra(PLAYDATA_KEY, playList);
                    //获取本地广播管理器
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(itemViewHolder.itemView.getContext());
                    manager.sendBroadcast(intent);
                }
            }
        });

    }
    class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_album;
        TextView tv_number;
        ImageView iv_playStatus;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_playName);
            tv_album=(TextView)itemView.findViewById(R.id.tv_playAlbum);
            tv_number=(TextView)itemView.findViewById(R.id.tv_number);
            iv_playStatus=(ImageView)itemView.findViewById(R.id.iv_playStatus);
        }
    }
    @Override
    public int getItemCount() {
        return playList.getMusics().size()+(headerView!=null?1:0)+(footerView!=null?1:0);
    }

    @Override
    public int getItemViewType(int position) {
        if(headerView!=null&&position==0){
            return TYPE_HEADER;
        }
        if(footerView!=null&&position==getItemCount()-1){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


}
