package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-7.
 */

public class HelpContentBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"content":"投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南"}
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
         * content : 投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南投资方指南
         */

        private String content;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
