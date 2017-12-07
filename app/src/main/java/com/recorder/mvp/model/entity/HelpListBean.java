package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-12-7.
 */

public class HelpListBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"list":[{"helperID":"0f1ff468fe29299260f52502","title":"投资方指南"},{"helperID":"2600da732b9260f5edde2602","title":"融资方指南"},{"helperID":"081b0cc863f5eddedc4f2702","title":"协议汇总"},{"helperID":"dea045afe9dedc4f365f1802","title":"风险提示"}]}
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
         * list : [{"helperID":"0f1ff468fe29299260f52502","title":"投资方指南"},{"helperID":"2600da732b9260f5edde2602","title":"融资方指南"},{"helperID":"081b0cc863f5eddedc4f2702","title":"协议汇总"},{"helperID":"dea045afe9dedc4f365f1802","title":"风险提示"}]
         */

        private List<ListEntity> list;

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            /**
             * helperID : 0f1ff468fe29299260f52502
             * title : 投资方指南
             */

            private String helperID;
            private String title;

            public void setHelperID(String helperID) {
                this.helperID = helperID;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getHelperID() {
                return helperID;
            }

            public String getTitle() {
                return title;
            }
        }
    }
}
