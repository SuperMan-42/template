package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-8.
 */

public class UserAuthInfoBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"zc_auth":{"type":"1","status":"0","file_status":"0","cn_status":"待认证","cn_file_status":"资产未审核"},"conformity_auth":{"type":"2","status":"4","file_status":"2","cn_status":"已认证","cn_file_status":"资产已审核"},"organ_auth":{"type":"3","status":"4","file_status":"3","cn_status":"已认证","cn_file_status":"资产审核失败"}}
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
         * zc_auth : {"type":"1","status":"0","file_status":"0","cn_status":"待认证","cn_file_status":"资产未审核"}
         * conformity_auth : {"type":"2","status":"4","file_status":"2","cn_status":"已认证","cn_file_status":"资产已审核"}
         * organ_auth : {"type":"3","status":"4","file_status":"3","cn_status":"已认证","cn_file_status":"资产审核失败"}
         */

        private ZcAuthEntity zc_auth;
        private ConformityAuthEntity conformity_auth;
        private OrganAuthEntity organ_auth;

        public void setZc_auth(ZcAuthEntity zc_auth) {
            this.zc_auth = zc_auth;
        }

        public void setConformity_auth(ConformityAuthEntity conformity_auth) {
            this.conformity_auth = conformity_auth;
        }

        public void setOrgan_auth(OrganAuthEntity organ_auth) {
            this.organ_auth = organ_auth;
        }

        public ZcAuthEntity getZc_auth() {
            return zc_auth;
        }

        public ConformityAuthEntity getConformity_auth() {
            return conformity_auth;
        }

        public OrganAuthEntity getOrgan_auth() {
            return organ_auth;
        }

        public static class ZcAuthEntity {
            /**
             * type : 1
             * status : 0
             * file_status : 0
             * cn_status : 待认证
             * cn_file_status : 资产未审核
             */

            private int type;
            private int status;
            private int file_status;
            private String cn_status;
            private String cn_file_status;

            public void setType(int type) {
                this.type = type;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setFile_status(int file_status) {
                this.file_status = file_status;
            }

            public void setCn_status(String cn_status) {
                this.cn_status = cn_status;
            }

            public void setCn_file_status(String cn_file_status) {
                this.cn_file_status = cn_file_status;
            }

            public int getType() {
                return type;
            }

            public int getStatus() {
                return status;
            }

            public int getFile_status() {
                return file_status;
            }

            public String getCn_status() {
                return cn_status;
            }

            public String getCn_file_status() {
                return cn_file_status;
            }
        }

        public static class ConformityAuthEntity {
            /**
             * type : 2
             * status : 4
             * file_status : 2
             * cn_status : 已认证
             * cn_file_status : 资产已审核
             */

            private int type;
            private int status;
            private int file_status;
            private String cn_status;
            private String cn_file_status;

            public void setType(int type) {
                this.type = type;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setFile_status(int file_status) {
                this.file_status = file_status;
            }

            public void setCn_status(String cn_status) {
                this.cn_status = cn_status;
            }

            public void setCn_file_status(String cn_file_status) {
                this.cn_file_status = cn_file_status;
            }

            public int getType() {
                return type;
            }

            public int getStatus() {
                return status;
            }

            public int getFile_status() {
                return file_status;
            }

            public String getCn_status() {
                return cn_status;
            }

            public String getCn_file_status() {
                return cn_file_status;
            }
        }

        public static class OrganAuthEntity {
            /**
             * type : 3
             * status : 4
             * file_status : 3
             * cn_status : 已认证
             * cn_file_status : 资产审核失败
             */

            private int type;
            private int status;
            private int file_status;
            private String cn_status;
            private String cn_file_status;

            public void setType(int type) {
                this.type = type;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setFile_status(int file_status) {
                this.file_status = file_status;
            }

            public void setCn_status(String cn_status) {
                this.cn_status = cn_status;
            }

            public void setCn_file_status(String cn_file_status) {
                this.cn_file_status = cn_file_status;
            }

            public int getType() {
                return type;
            }

            public int getStatus() {
                return status;
            }

            public int getFile_status() {
                return file_status;
            }

            public String getCn_status() {
                return cn_status;
            }

            public String getCn_file_status() {
                return cn_file_status;
            }
        }
    }
}
