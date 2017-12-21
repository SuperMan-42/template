package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-21.
 */

public class AuthBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"survey_host":"http://wap-test.dreamflyc.com/survey/organ-detail"}
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
         * survey_host : http://wap-test.dreamflyc.com/survey/organ-detail
         */

        private String survey_host;

        public void setSurvey_host(String survey_host) {
            this.survey_host = survey_host;
        }

        public String getSurvey_host() {
            return survey_host;
        }
    }
}
