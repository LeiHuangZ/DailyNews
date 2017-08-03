package com.zhuoxin.dailynews.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class UserInfo {

    /**
     * message : OK
     * status : 0
     * data : {"uid":"huangleily1","integration":0,"loginlog":[{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"广东","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"北京","device":0}],"comnum":0,"portrait":"http://118.244.212.82:9092/Images/image.png"}
     */

    private String message;
    private int status;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : huangleily1
         * integration : 0
         * loginlog : [{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"广东","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"上海","device":0},{"time":"2017-07-24 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"上海","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"广东","device":0},{"time":"2017-07-25 00:00:00.0","address":"北京","device":0}]
         * comnum : 0
         * portrait : http://118.244.212.82:9092/Images/image.png
         */

        private String uid;
        private int integration;
        private int comnum;
        private String portrait;
        private List<LoginlogBean> loginlog;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getIntegration() {
            return integration;
        }

        public void setIntegration(int integration) {
            this.integration = integration;
        }

        public int getComnum() {
            return comnum;
        }

        public void setComnum(int comnum) {
            this.comnum = comnum;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public List<LoginlogBean> getLoginlog() {
            return loginlog;
        }

        public void setLoginlog(List<LoginlogBean> loginlog) {
            this.loginlog = loginlog;
        }

        public static class LoginlogBean {
            /**
             * time : 2017-07-24 00:00:00.0
             * address : 上海
             * device : 0
             */

            private String time;
            private String address;
            private int device;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getDevice() {
                return device;
            }

            public void setDevice(int device) {
                this.device = device;
            }
        }
    }
}
