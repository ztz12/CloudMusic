package com.ztz.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wqewqe on 2017/6/11.
 */

public class PlayListResponse  {


    /**
     * results : [{"fileUrl":{"name":"吴若希 - 爱我请留言.mp3","url":"http://ac-kCFRDdr9.clouddn.com/EpaoJkSd65rzul2488ZwKITP1Q7mxe5HIQR0kLfs.mp3","objectId":"593c3262ac502e006b37aebd","__type":"File","provider":"qiniu"},"albumId":221,"displayName":"吴若希 - 爱我请留言.mp3","objectId":"593c326a128fe1006add1b6d","duration":233535,"artist":"吴若希","albumPic":{"name":"6011030069315393.jpg","url":"http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg","objectId":"593f9e8f61ff4b006caa656f","__type":"File","provider":"qiniu"},"size":3738638,"title":"爱我请留言","id":48337,"album":"爱我请留言"}]
     * className : Song
     */

    private String className;
    private List<ResultsBean> results;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Parcelable{
        /**
         * fileUrl : {"name":"吴若希 - 爱我请留言.mp3","url":"http://ac-kCFRDdr9.clouddn.com/EpaoJkSd65rzul2488ZwKITP1Q7mxe5HIQR0kLfs.mp3","objectId":"593c3262ac502e006b37aebd","__type":"File","provider":"qiniu"}
         * albumId : 221
         * displayName : 吴若希 - 爱我请留言.mp3
         * objectId : 593c326a128fe1006add1b6d
         * duration : 233535
         * artist : 吴若希
         * albumPic : {"name":"6011030069315393.jpg","url":"http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg","objectId":"593f9e8f61ff4b006caa656f","__type":"File","provider":"qiniu"}
         * size : 3738638
         * title : 爱我请留言
         * id : 48337
         * album : 爱我请留言
         */

        private FileUrlBean fileUrl;
        private int albumId;
        private String displayName;
        private String objectId;
        private int duration;
        private String artist;
        private AlbumPicBean albumPic;
        private int size;
        private String title;
        private int id;
        private String album;
        private boolean isPlayStatus=false;

        protected ResultsBean(Parcel in) {
            fileUrl = in.readParcelable(FileUrlBean.class.getClassLoader());
            albumId = in.readInt();
            displayName = in.readString();
            objectId = in.readString();
            duration = in.readInt();
            artist = in.readString();
            albumPic = in.readParcelable(AlbumPicBean.class.getClassLoader());
            size = in.readInt();
            title = in.readString();
            id = in.readInt();
            album = in.readString();
            isPlayStatus = in.readByte() != 0;
        }

        public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel in) {
                return new ResultsBean(in);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
            }
        };

        public boolean isPlayStatus() {
            return isPlayStatus;
        }

        public void setPlayStatus(boolean playStatus) {
            isPlayStatus = playStatus;
        }

        public FileUrlBean getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(FileUrlBean fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public AlbumPicBean getAlbumPic() {
            return albumPic;
        }

        public void setAlbumPic(AlbumPicBean albumPic) {
            this.albumPic = albumPic;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(fileUrl, flags);
            dest.writeInt(albumId);
            dest.writeString(displayName);
            dest.writeString(objectId);
            dest.writeInt(duration);
            dest.writeString(artist);
            dest.writeParcelable(albumPic, flags);
            dest.writeInt(size);
            dest.writeString(title);
            dest.writeInt(id);
            dest.writeString(album);
            dest.writeByte((byte) (isPlayStatus ? 1 : 0));
        }

        public static class FileUrlBean implements Parcelable{
            /**
             * name : 吴若希 - 爱我请留言.mp3
             * url : http://ac-kCFRDdr9.clouddn.com/EpaoJkSd65rzul2488ZwKITP1Q7mxe5HIQR0kLfs.mp3
             * objectId : 593c3262ac502e006b37aebd
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

            protected FileUrlBean(Parcel in) {
                name = in.readString();
                url = in.readString();
                objectId = in.readString();
                __type = in.readString();
                provider = in.readString();
            }

            public static final Creator<FileUrlBean> CREATOR = new Creator<FileUrlBean>() {
                @Override
                public FileUrlBean createFromParcel(Parcel in) {
                    return new FileUrlBean(in);
                }

                @Override
                public FileUrlBean[] newArray(int size) {
                    return new FileUrlBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeString(url);
                dest.writeString(objectId);
                dest.writeString(__type);
                dest.writeString(provider);
            }
        }

        public static class AlbumPicBean implements Parcelable{
            /**
             * name : 6011030069315393.jpg
             * url : http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg
             * objectId : 593f9e8f61ff4b006caa656f
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

            protected AlbumPicBean(Parcel in) {
                name = in.readString();
                url = in.readString();
                objectId = in.readString();
                __type = in.readString();
                provider = in.readString();
            }

            public static final Creator<AlbumPicBean> CREATOR = new Creator<AlbumPicBean>() {
                @Override
                public AlbumPicBean createFromParcel(Parcel in) {
                    return new AlbumPicBean(in);
                }

                @Override
                public AlbumPicBean[] newArray(int size) {
                    return new AlbumPicBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeString(url);
                dest.writeString(objectId);
                dest.writeString(__type);
                dest.writeString(provider);
            }
        }
    }
}
