package com.ztz.cloudmusic.bean;

/**
 * Created by wqewqe on 2017/6/22.
 */

public class LrcBeen {
    private String content;
    private long startTime;//开始时间
    private long endTime;//结束时间

    public LrcBeen(String content, long startTime, long endTime) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "LrcBeen{" +
                "content='" + content + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
