package com.zhuoxin.dailynews.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class Comments {
    /**
     * message : OK
     * status : 0
     * data : [{"uid":"whj24","content":"nihaodfaf","stamp":"2017-07-11 11:37:57","cid":12791,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zzzzzzzzzzzzzz","content":"six","stamp":"2017-07-07 09:28:15","cid":12774,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zzzzzzzzzzzz","content":"ssss","stamp":"2017-07-06 17:02:32","cid":12766,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"wxy","content":"111","stamp":"2017-07-03 17:57:24","cid":12699,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"wxy","content":"111","stamp":"2017-07-03 17:32:51","cid":12663,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"wxy","content":"666","stamp":"2017-07-03 17:29:29","cid":12652,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":".gghg","stamp":"2017-07-03 11:34:00","cid":12636,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":"ga","stamp":"2017-06-26 19:41:45","cid":12579,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"DorctorWei333","content":"wwww","stamp":"2017-06-26 19:35:04","cid":12578,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"DoctorWei666","content":"AAA","stamp":"2017-06-26 19:20:19","cid":12577,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":"kja","stamp":"2017-06-23 18:14:22","cid":12572,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":"kja","stamp":"2017-06-23 18:14:21","cid":12571,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":"hfhf","stamp":"2017-06-22 19:37:24","cid":12563,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"zxcvbnm12","content":"ga","stamp":"2017-06-22 09:51:44","cid":12559,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"qwepoi","content":"nihao","stamp":"2017-06-21 20:26:14","cid":12554,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"qwepoi","content":"heihei","stamp":"2017-06-21 20:25:47","cid":12553,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"qwepoi","content":"haha","stamp":"2017-06-21 20:24:55","cid":12552,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"qwepoi","content":"hehe","stamp":"2017-06-21 20:17:25","cid":12551,"portrait":"http://118.244.212.82:9092/Images/image.png"},{"uid":"1","content":"1","stamp":"2017-06-14 14:04:26","cid":12508,"portrait":"http://118.244.212.82:9092/Images/20160629051915.jpg"},{"uid":"1","content":"1","stamp":"2017-06-14 14:04:26","cid":12509,"portrait":"http://118.244.212.82:9092/Images/20160629051915.jpg"}]
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : whj24
         * content : nihaodfaf
         * stamp : 2017-07-11 11:37:57
         * cid : 12791
         * portrait : http://118.244.212.82:9092/Images/image.png
         */

        private String uid;
        private String content;
        private String stamp;
        private int cid;
        private String portrait;

        public DataBean(String uid, String content, String stamp, String portrait) {
            this.uid = uid;
            this.content = content;
            this.stamp = stamp;
            this.portrait = portrait;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStamp() {
            return stamp;
        }

        public void setStamp(String stamp) {
            this.stamp = stamp;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }
}
