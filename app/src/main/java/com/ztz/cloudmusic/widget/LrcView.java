package com.ztz.cloudmusic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ztz.cloudmusic.bean.LrcBeen;
import com.ztz.cloudmusic.service.MusicService;

import java.util.ArrayList;

/**
 * Created by wqewqe on 2017/6/22.
 */

public class LrcView extends View {
    ArrayList<LrcBeen> lrcBeens;
    int mWidth=0;
    int mHeight=0;
    Paint mPaint;
    Paint mHPaint;
    public LrcView(Context context) {
        this(context,null);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(30);

        //高亮
        mHPaint=new Paint();
        mHPaint.setColor(Color.RED);
        mHPaint.setAntiAlias(true);//抗锯齿
        mHPaint.setTextAlign(Paint.Align.CENTER);
        mHPaint.setTextSize(30);
    }
    public void setLrcData(ArrayList<LrcBeen> lrcData){
        this.lrcBeens=lrcData;
        invalidate();

    }
    int lastPosition=0;
    //当前播放的位置
    int currPosition=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth=getWidth();
        mHeight=getHeight();
        if(lrcBeens==null||lrcBeens.isEmpty()){
            canvas.drawText("没有歌词",mWidth/2,mHeight/2,mPaint);
            return;
        }
        //获取当前播放的进度
        int currMills= MusicService.getCurrPosition();
        for(int i=0;i<lrcBeens.size();i++) {
            if (currMills >= lrcBeens.get(i).getStartTime() && currMills < lrcBeens.get(i).getEndTime()) {
                currPosition=i;
            }
        }
        drawCanvas(canvas);
        long startTime=lrcBeens.get(currPosition).getStartTime();
        int y=currMills>startTime?currPosition*50:lastPosition*50;
        setScrollY(y);
        lastPosition=currPosition;
        postInvalidateDelayed(100);

    }

    private void drawCanvas(Canvas canvas) {
        for(int i=0;i<lrcBeens.size();i++){
            if(i==currPosition) {
                canvas.drawText(lrcBeens.get(i).getContent(), mWidth / 2, mHeight / 2 + 50 * i, mHPaint);
            }else {
                canvas.drawText(lrcBeens.get(i).getContent(),mWidth/2,mHeight/2+50*i,mPaint);
            }
        }
    }
}
