package com.ztz.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.bean.Home;
import com.ztz.cloudmusic.fragment.found.RecommendFragment;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdjustColumnActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.column_rl)
    RecyclerView columnRl;
    ArrayList<Home> homeArrayList=new ArrayList<>();
    AdjustAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_colum);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdjustColumnActivity.this, RecommendFragment.class);
                intent.putParcelableArrayListExtra("homes",homeArrayList);
                setResult(222,intent);//结果码为222
                finish();
            }
        });
        columnRl=(RecyclerView)findViewById(R.id.column_rl);
        columnRl.setLayoutManager(new LinearLayoutManager(this));
        homeArrayList=getIntent().getParcelableArrayListExtra("homes");
        adapter=new AdjustAdapter();
        columnRl.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                int swipeFlags=0;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition=viewHolder.getAdapterPosition();
                int toPosition=target.getAdapterPosition();
                adapter.notifyItemMoved(fromPosition,toPosition);
                Collections.swap(homeArrayList,fromPosition,toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(columnRl);

    }
    class AdjustAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adjust,parent,false);
            MyViewHolder holder=new MyViewHolder(view);
            return holder;
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv=(TextView)itemView.findViewById(R.id.tv_adjust);
            }
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder= (MyViewHolder) holder;
            myViewHolder.tv.setText(homeArrayList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return homeArrayList.size();
        }
    }
}
