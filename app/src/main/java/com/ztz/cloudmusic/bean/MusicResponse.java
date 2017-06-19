package com.ztz.cloudmusic.bean;

import java.util.List;

/**
 * Created by wqewqe on 2017/6/13.
 */

public class MusicResponse {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * desc :
         * audienceSize : 15
         * name : 故人长绝，却余三月樱
         * picUrl : {"name":"1496942418319.jpg","url":"http://p3.music.126.net/2vHrwmMBxM9zmX070dq-2w==/18656513302029883.jpg","objectId":"59398756ac502e006b1d2f2b","__type":"File","provider":"qiniu"}
         * objectId : 593987565c497d006b68d03a
         * songList : {"__type":"Relation","className":"Song"}
         * limit : 10
         */

        private String desc;
        private String audienceSize;
        private String name;
        private PicUrlBean picUrl;
        private String objectId;
        private SongListBean songList;
        private int limit;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAudienceSize() {
            return audienceSize;
        }

        public void setAudienceSize(String audienceSize) {
            this.audienceSize = audienceSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PicUrlBean getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(PicUrlBean picUrl) {
            this.picUrl = picUrl;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public SongListBean getSongList() {
            return songList;
        }

        public void setSongList(SongListBean songList) {
            this.songList = songList;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public static class PicUrlBean {
            /**
             * name : 1496942418319.jpg
             * url : http://p3.music.126.net/2vHrwmMBxM9zmX070dq-2w==/18656513302029883.jpg
             * objectId : 59398756ac502e006b1d2f2b
             * __type : File
             * provider : qiniu
             */

            private String name;
            private String url;
            private String objectId;
            private String __type;
            private String provider;

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
    }
}
