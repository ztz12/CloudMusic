package com.ztz.cloudmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztz.cloudmusic.R;
import com.ztz.cloudmusic.adapter.HomeAdapter;
import com.ztz.cloudmusic.fragment.found.PlayListFragment;
import com.ztz.cloudmusic.fragment.found.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class FoundFragment extends Fragment {

    private ViewPager vp;
    private TabLayout tb;
    List<FoundItem> foundItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_found,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vp = (ViewPager)view.findViewById(R.id.vp_found);
        tb = (TabLayout) view.findViewById(R.id.tab_found);
        foundItemList=new ArrayList<>();
        RecommendFragment recommendFragment=new RecommendFragment();
        foundItemList.add(new FoundItem("个性推荐",recommendFragment));
        foundItemList.add(new FoundItem("歌单",new PlayListFragment()));
        FoundAdapter adapter=new FoundAdapter(getFragmentManager());
        vp.setAdapter(adapter);
        tb.setupWithViewPager(vp);
        recommendFragment.setItemSelector(new HomeAdapter.RecommendedItemSelector() {
            @Override
            public void selector() {
                vp.setCurrentItem(1);
            }
        });
    }
    class FoundAdapter extends FragmentPagerAdapter{

        public FoundAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return foundItemList.get(position).getF();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return foundItemList.get(position).getTitle();
        }
        @Override
        public int getCount() {
            return foundItemList.size();
        }
    }
    /**
     * 封装item数据
     */
   public class FoundItem{
        String title;
        Fragment f;

        public FoundItem(String title, Fragment f) {
            this.title = title;
            this.f = f;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Fragment getF() {
            return f;
        }

        public void setF(Fragment f) {
            this.f = f;
        }
    }
}
