package com.ztz.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wqewqe on 2017/6/19.
 */

public class PlayList implements Parcelable {
    //歌曲id；
    private String objectId;
    //歌曲列表
    ArrayList<Music> musics = new ArrayList<>();

    public PlayList() {

    }

    protected PlayList(Parcel in) {
        objectId = in.readString();
        musics = in.createTypedArrayList(Music.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeTypedList(musics);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayList> CREATOR = new Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel in) {
            return new PlayList(in);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }


    /**
     * 歌曲类
     */
    public static class Music implements Parcelable {
        //唯一id
        private String objectId;

        //显示名字
        private String title;

        //艺术家
        private String artist;

        //音乐地址
        String musicUrl;

        //专辑图片
        private String albumPicUrl;

        //专辑名称
        private String album;

        //歌词下载的链接
        private String lrcUrl;

        //播放状态  true 播放   false 未播放
        private boolean playStatus = false;

        public Music(String objectId,
                     String title,
                     String artist,
                     String musicUrl,
                     String albumPicUrl,
                     String album,
                     String lrcUrl) {
            this.objectId = objectId;
            this.title = title;
            this.artist = artist;
            this.musicUrl = musicUrl;
            this.albumPicUrl = albumPicUrl;
            this.album = album;
            this.lrcUrl = lrcUrl;
        }

        protected Music(Parcel in) {
            objectId = in.readString();
            title = in.readString();
            artist = in.readString();
            musicUrl = in.readString();
            albumPicUrl = in.readString();
            album = in.readString();
            lrcUrl = in.readString();
            playStatus = in.readByte() != 0;
        }

        public static final Creator<Music> CREATOR = new Creator<Music>() {
            @Override
            public Music createFromParcel(Parcel in) {
                return new Music(in);
            }

            @Override
            public Music[] newArray(int size) {
                return new Music[size];
            }
        };

        public String getLrcUrl() {
            return lrcUrl;
        }

        public void setLrcUrl(String lrcUrl) {
            this.lrcUrl = lrcUrl;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String getAlbumPicUrl() {
            return albumPicUrl;
        }

        public void setAlbumPicUrl(String albumPicUrl) {
            this.albumPicUrl = albumPicUrl;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public boolean isPlayStatus() {
            return playStatus;
        }

        public void setPlayStatus(boolean playStatus) {
            this.playStatus = playStatus;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(objectId);
            dest.writeString(title);
            dest.writeString(artist);
            dest.writeString(musicUrl);
            dest.writeString(albumPicUrl);
            dest.writeString(album);
            dest.writeString(lrcUrl);
            dest.writeByte((byte) (playStatus ? 1 : 0));
        }
    }
}
