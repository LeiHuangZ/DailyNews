package com.zhuoxin.dailynews.bean;

/**
 * Created by Administrator on 2017/7/31.
 */

public class RegisterInfo {
    /**
     * message : OK
     * status : 0
     * data : {"result":0,"token":"594a3a60eb6bb101b3210ec4ec05c6c0","explain":"注册成功"}
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
         * result : 0
         * token : 594a3a60eb6bb101b3210ec4ec05c6c0
         * explain : 注册成功
         */

        private int result;
        private String token;
        private String explain;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }
    }
}
