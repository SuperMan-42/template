package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-11-25.
 */

public class UserInfoBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"userID":"722979e7bbedfb0d5994bbb9","user_id":"3","user_name":"用户K6400920","avatar":"","auth_type":"0","intro":"","email":"","address":"","weixin":"","mobile":"185****0920","my_investment":"0","my_follow_count":"0","post_investment":"0"}
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
         * user_id : 3
         * user_name : 用户K6400920
         * avatar :
         * auth_type : 0
         * intro :
         * email :
         * address :
         * weixin :
         * mobile : 185****0920
         * my_investment : 0
         * my_follow_count : 0
         * post_investment : 0
         */

        private String userID;
        private String user_id;
        private String user_name;
        private String avatar;
        private String auth_type;
        private String intro;
        private String email;
        private String address;
        private String weixin;
        private String mobile;
        private String my_investment;
        private String my_follow_count;
        private String post_investment;

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setAuth_type(String auth_type) {
            this.auth_type = auth_type;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setMy_investment(String my_investment) {
            this.my_investment = my_investment;
        }

        public void setMy_follow_count(String my_follow_count) {
            this.my_follow_count = my_follow_count;
        }

        public void setPost_investment(String post_investment) {
            this.post_investment = post_investment;
        }

        public String getUserID() {
            return userID;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getAuth_type() {
            return auth_type;
        }

        public String getIntro() {
            return intro;
        }

        public String getEmail() {
            return email;
        }

        public String getAddress() {
            return address;
        }

        public String getWeixin() {
            return weixin;
        }

        public String getMobile() {
            return mobile;
        }

        public String getMy_investment() {
            return my_investment;
        }

        public String getMy_follow_count() {
            return my_follow_count;
        }

        public String getPost_investment() {
            return post_investment;
        }
    }
}
