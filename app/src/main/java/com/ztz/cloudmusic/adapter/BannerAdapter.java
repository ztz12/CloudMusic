package com.ztz.cloudmusic.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.image.SmartImageView;
import com.ztz.cloudmusic.bean.Result;

import java.util.List;

/**
 * Created by wqewqe on 2017/6/8.
 */

public class BannerAdapter extends PagerAdapter {
   List<Result> resultList;

    public BannerAdapter(List<Result> resultList) {
        this.resultList = resultList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       //获取图片链接;
        SmartImageView smartImageView=resultList.get(position).getSmartImageView();
        //通过图片链接加载图片;
        smartImageView.setImageUrl(resultList.get(position).getPicurl());
        container.addView(smartImageView);
        return smartImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(resultList.get(position).getSmartImageView());
    }
}
