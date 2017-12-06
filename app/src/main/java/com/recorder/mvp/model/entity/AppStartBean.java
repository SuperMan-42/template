package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-6.
 */

public class AppStartBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"sp_img":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779800.jpg","service_tel":"400-888-888","user_auth_prompt":{"zc_auth":"勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。","conformity_auth":"勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。","organ_auth":"勾选并承诺净资产不低于1000万元的单位或者其他符合证监会规定的合格投资者"}}
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
         * sp_img : http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779800.jpg
         * service_tel : 400-888-888
         * user_auth_prompt : {"zc_auth":"勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。","conformity_auth":"勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。","organ_auth":"勾选并承诺净资产不低于1000万元的单位或者其他符合证监会规定的合格投资者"}
         */

        private String sp_img;
        private String service_tel;
        private UserAuthPromptEntity user_auth_prompt;

        public void setSp_img(String sp_img) {
            this.sp_img = sp_img;
        }

        public void setService_tel(String service_tel) {
            this.service_tel = service_tel;
        }

        public void setUser_auth_prompt(UserAuthPromptEntity user_auth_prompt) {
            this.user_auth_prompt = user_auth_prompt;
        }

        public String getSp_img() {
            return sp_img;
        }

        public String getService_tel() {
            return service_tel;
        }

        public UserAuthPromptEntity getUser_auth_prompt() {
            return user_auth_prompt;
        }

        public static class UserAuthPromptEntity {
            /**
             * zc_auth : 勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。
             * conformity_auth : 勾选并承诺金融资产不低于300万元或者最近三年个人年均收入不低于50万元。
             * organ_auth : 勾选并承诺净资产不低于1000万元的单位或者其他符合证监会规定的合格投资者
             */

            private String zc_auth;
            private String conformity_auth;
            private String organ_auth;

            public void setZc_auth(String zc_auth) {
                this.zc_auth = zc_auth;
            }

            public void setConformity_auth(String conformity_auth) {
                this.conformity_auth = conformity_auth;
            }

            public void setOrgan_auth(String organ_auth) {
                this.organ_auth = organ_auth;
            }

            public String getZc_auth() {
                return zc_auth;
            }

            public String getConformity_auth() {
                return conformity_auth;
            }

            public String getOrgan_auth() {
                return organ_auth;
            }
        }
    }
}
