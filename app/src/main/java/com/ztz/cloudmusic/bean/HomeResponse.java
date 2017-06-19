package com.ztz.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wqewqe on 2017/6/9.
 */

public class HomeResponse {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * type : 2
         * Item : 最新音乐
         * createdAt : 2017-05-31T17:18:39.171Z
         * updatedAt : 2017-06-07T12:14:52.148Z
         * playList : {"updatedAt":"2017-06-07T17:54:38.455Z","songList":{"__type":"Relation","className":"Song"},"objectId":"5937ee2da0bb9f0058097304","createdAt":"2017-06-07T12:14:37.665Z","type":2,"playListName":"毕业季 | 让我与你握别这无悔的青春","className":"NewPlayList","author":{"updatedAt":"2017-06-06T12:15:27.777Z","objectId":"59369cdfac502e0068c28a37","username":"我是大魔王","createdAt":"2017-06-06T12:15:27.777Z","emailVerified":false,"mobilePhoneVerified":false,"__type":"Pointer","className":"_User"},"__type":"Pointer","picUrl":"http://or49nvrps.bkt.clouddn.com/109951162940555974.jpg"}
         * objectId : 592efaefa22b9d00577a6c1f
         */

        private int type;
        private String Item;
        private String createdAt;
        private String updatedAt;
        private PlayListBean playList;
        private String objectId;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getItem() {
            return Item;
        }

        public void setItem(String Item) {
            this.Item = Item;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public PlayListBean getPlayList() {
            return playList;
        }

        public void setPlayList(PlayListBean playList) {
            this.playList = playList;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public static class PlayListBean implements Parcelable {
            /**
             * updatedAt : 2017-06-07T17:54:38.455Z
             * songList : {"__type":"Relation","className":"Song"}
             * objectId : 5937ee2da0bb9f0058097304
             * createdAt : 2017-06-07T12:14:37.665Z
             * type : 2
             * playListName : 毕业季 | 让我与你握别这无悔的青春
             * className : NewPlayList
             * author : {"updatedAt":"2017-06-06T12:15:27.777Z","objectId":"59369cdfac502e0068c28a37","username":"我是大魔王","createdAt":"2017-06-06T12:15:27.777Z","emailVerified":false,"mobilePhoneVerified":false,"__type":"Pointer","className":"_User"}
             * __type : Pointer
             * picUrl : http://or49nvrps.bkt.clouddn.com/109951162940555974.jpg
             */

            private String updatedAt;
            private SongListBean songList;
            private String objectId;
            private String createdAt;
            private int type;
            private String playListName;
            private String className;
            private AuthorBean author;
            private String __type;
            private String picUrl;

            protected PlayListBean(Parcel in) {
                updatedAt = in.readString();
                objectId = in.readString();
                createdAt = in.readString();
                type = in.readInt();
                playListName = in.readString();
                className = in.readString();
                __type = in.readString();
                picUrl = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(updatedAt);
                dest.writeString(objectId);
                dest.writeString(createdAt);
                dest.writeInt(type);
                dest.writeString(playListName);
                dest.writeString(className);
                dest.writeString(__type);
                dest.writeString(picUrl);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<PlayListBean> CREATOR = new Creator<PlayListBean>() {
                @Override
                public PlayListBean createFromParcel(Parcel in) {
                    return new PlayListBean(in);
                }

                @Override
                public PlayListBean[] newArray(int size) {
                    return new PlayListBean[size];
                }
            };

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public SongListBean getSongList() {
                return songList;
            }

            public void setSongList(SongListBean songList) {
                this.songList = songList;
            }

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPlayListName() {
                return playListName;
            }

            public void setPlayListName(String playListName) {
                this.playListName = playListName;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public static class SongListBean {
                /**
                 * __type : Relation
                 * className : Song
                 */

                private String __type;
                private String className;

                public String get__type() {
                    return __type;
                }

                public void set__type(String __type) {
                    this.__type = __type;
                }

                public String getClassName() {
                    return className;
                }

                public void setClassName(String className) {
                    this.className = className;
                }
            }

            public static class AuthorBean {
                /**
                 * updatedAt : 2017-06-06T12:15:27.777Z
                 * objectId : 59369cdfac502e0068c28a37
                 * username : 我是大魔王
                 * createdAt : 2017-06-06T12:15:27.777Z
                 * emailVerified : false
                 * mobilePhoneVerified : false
                 * __type : Pointer
                 * className : _User
                 */

                private String updatedAt;
                private String objectId;
                private String username;
                private String createdAt;
                private boolean emailVerified;
                private boolean mobilePhoneVerified;
                private String __type;
                private String className;

                public String getUpdatedAt() {
                    return updatedAt;
                }

                public void setUpdatedAt(String updatedAt) {
                    this.updatedAt = updatedAt;
                }

                public String getObjectId() {
                    return objectId;
                }

                public void setObjectId(String objectId) {
                    this.objectId = objectId;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

                public boolean isEmailVerified() {
                    return emailVerified;
                }

                public void setEmailVerified(boolean emailVerified) {
                    this.emailVerified = emailVerified;
                }

                public boolean isMobilePhoneVerified() {
                    return mobilePhoneVerified;
                }

                public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
                    this.mobilePhoneVerified = mobilePhoneVerified;
                }

                public String get__type() {
                    return __type;
                }

                public void set__type(String __type) {
                    this.__type = __type;
                }

                public String getClassName() {
                    return className;
                }

                public void setClassName(String className) {
                    this.className = className;
                }
            }
        }
    }
}
