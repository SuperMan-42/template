package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-12-9.
 */

public class AppVersionBean {

    /**
     * errno : 0
     * error :
     * data : {"version_info":{"new_version":"2.0","des":["1.修改若干bug","2.FA列表增加筛选项"],"is_force":1,"download":"http://api.local.bole.cn/upload/app/yuanshihui.apk?v=1232342342"}}
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
         * version_info : {"new_version":"2.0","des":["1.修改若干bug","2.FA列表增加筛选项"],"is_force":1,"download":"http://api.local.bole.cn/upload/app/yuanshihui.apk?v=1232342342"}
         */

        private VersionInfoEntity version_info;

        public void setVersion_info(VersionInfoEntity version_info) {
            this.version_info = version_info;
        }

        public VersionInfoEntity getVersion_info() {
            return version_info;
        }

        public static class VersionInfoEntity {
            /**
             * new_version : 2.0
             * des : ["1.修改若干bug","2.FA列表增加筛选项"]
             * is_force : 1
             * download : http://api.local.bole.cn/upload/app/yuanshihui.apk?v=1232342342
             */

            private String new_version;
            private int is_force;
            private String download;
            private List<String> des;

            public void setNew_version(String new_version) {
                this.new_version = new_version;
            }

            public void setIs_force(int is_force) {
                this.is_force = is_force;
            }

            public void setDownload(String download) {
                this.download = download;
            }

            public void setDes(List<String> des) {
                this.des = des;
            }

            public String getNew_version() {
                return new_version;
            }

            public int getIs_force() {
                return is_force;
            }

            public String getDownload() {
                return download;
            }

            public List<String> getDes() {
                return des;
            }
        }
    }
}
