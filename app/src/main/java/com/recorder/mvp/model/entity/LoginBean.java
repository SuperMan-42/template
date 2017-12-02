package com.recorder.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by hpw on 17-11-23.
 */

public class LoginBean implements Serializable {

    /**
     * errno : 0
     * error : 成功
     * data : {"userID":"722979e7bbedfb0d5994bbb9","user_name":"用户K6400920","mobile":"18510510920","avatar":""}
     */

    private int errno;
    private String error;
    private DataEntity data;

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }

    public String getError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * userID : 722979e7bbedfb0d5994bbb9
         * user_name : 用户K6400920
         * mobile : 18510510920
         * avatar :
         */

        private String userID;
        private String user_name;
        private String mobile;
        private String avatar;

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserID() {
            return userID;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
