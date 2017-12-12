package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-12-8.
 */

public class AuthGetBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"true_name":"","id_card":"","idcard_imgf":"","idcard_imgb":"","organ_name":"安卓","organ_legal_person":"程序员","organ_contact":"13312361236","organ_license":"http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAGu9aAARMufEUSZ0161.jpg","check":"1","assets":["http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAaOjiAAVuR_06_ik521.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAY8NMAAb8TDA3KnM116.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAPT0eAAbTTpUCq10764.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAWoxKAANzFnj3Czs375.jpg"],"survey":"","is_modify":false,"is_modify_file":true,"is_modify_survey":true,"is_hint":"unsuccess"}
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
         * true_name :
         * id_card :
         * idcard_imgf :
         * idcard_imgb :
         * organ_name : 安卓
         * organ_legal_person : 程序员
         * organ_contact : 13312361236
         * organ_license : http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAGu9aAARMufEUSZ0161.jpg
         * check : 1
         * assets : ["http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAaOjiAAVuR_06_ik521.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAY8NMAAb8TDA3KnM116.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAPT0eAAbTTpUCq10764.jpg","http://ustatic-test.dreamflyc.com/group1/M00/5E/CA/ChRYqlovPcOAWoxKAANzFnj3Czs375.jpg"]
         * survey :
         * is_modify : false
         * is_modify_file : true
         * is_modify_survey : true
         * is_hint : unsuccess
         */

        private String true_name;
        private String id_card;
        private String idcard_imgf;
        private String idcard_imgb;
        private String organ_name;
        private String organ_legal_person;
        private String organ_contact;
        private String organ_license;
        private String check;
        private String survey;
        private boolean is_modify;
        private boolean is_modify_file;
        private boolean is_modify_survey;
        private String is_hint;
        private List<String> assets;

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public void setIdcard_imgf(String idcard_imgf) {
            this.idcard_imgf = idcard_imgf;
        }

        public void setIdcard_imgb(String idcard_imgb) {
            this.idcard_imgb = idcard_imgb;
        }

        public void setOrgan_name(String organ_name) {
            this.organ_name = organ_name;
        }

        public void setOrgan_legal_person(String organ_legal_person) {
            this.organ_legal_person = organ_legal_person;
        }

        public void setOrgan_contact(String organ_contact) {
            this.organ_contact = organ_contact;
        }

        public void setOrgan_license(String organ_license) {
            this.organ_license = organ_license;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public void setSurvey(String survey) {
            this.survey = survey;
        }

        public void setIs_modify(boolean is_modify) {
            this.is_modify = is_modify;
        }

        public void setIs_modify_file(boolean is_modify_file) {
            this.is_modify_file = is_modify_file;
        }

        public void setIs_modify_survey(boolean is_modify_survey) {
            this.is_modify_survey = is_modify_survey;
        }

        public void setIs_hint(String is_hint) {
            this.is_hint = is_hint;
        }

        public void setAssets(List<String> assets) {
            this.assets = assets;
        }

        public String getTrue_name() {
            return true_name;
        }

        public String getId_card() {
            return id_card;
        }

        public String getIdcard_imgf() {
            return idcard_imgf;
        }

        public String getIdcard_imgb() {
            return idcard_imgb;
        }

        public String getOrgan_name() {
            return organ_name;
        }

        public String getOrgan_legal_person() {
            return organ_legal_person;
        }

        public String getOrgan_contact() {
            return organ_contact;
        }

        public String getOrgan_license() {
            return organ_license;
        }

        public String getCheck() {
            return check;
        }

        public String getSurvey() {
            return survey;
        }

        public boolean getIs_modify() {
            return is_modify;
        }

        public boolean getIs_modify_file() {
            return is_modify_file;
        }

        public boolean getIs_modify_survey() {
            return is_modify_survey;
        }

        public String getIs_hint() {
            return is_hint;
        }

        public List<String> getAssets() {
            return assets;
        }
    }
}
