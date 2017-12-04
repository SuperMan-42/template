package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-4.
 */

public class PayCheckBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"dealid":"a37adb060e54843880d00da9","deal_name":"网络电影《人间大炮3》2","limit_price":"10","shakes":"0","manager_fee":"5","consult_fee":"0","subscription_fee":"0","partner_fee":"0","plat_manage_fee":"0","other_fee":"0","custom_fee_name":"0","custom_fee":"0"}
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
         * dealID : a37adb060e54843880d00da9
         * deal_name : 网络电影《人间大炮3》2
         * limit_price : 10
         * shakes : 0
         * manager_fee : 5
         * consult_fee : 0
         * subscription_fee : 0
         * partner_fee : 0
         * plat_manage_fee : 0
         * other_fee : 0
         * custom_fee_name : 0
         * custom_fee : 0
         */

        private String dealID;
        private String deal_name;
        private String limit_price;
        private String shakes;
        private String manager_fee;
        private String consult_fee;
        private String subscription_fee;
        private String partner_fee;
        private String plat_manage_fee;
        private String other_fee;
        private String custom_fee_name;
        private String custom_fee;

        public void setDealID(String dealID) {
            this.dealID = dealID;
        }

        public void setDeal_name(String deal_name) {
            this.deal_name = deal_name;
        }

        public void setLimit_price(String limit_price) {
            this.limit_price = limit_price;
        }

        public void setShakes(String shakes) {
            this.shakes = shakes;
        }

        public void setManager_fee(String manager_fee) {
            this.manager_fee = manager_fee;
        }

        public void setConsult_fee(String consult_fee) {
            this.consult_fee = consult_fee;
        }

        public void setSubscription_fee(String subscription_fee) {
            this.subscription_fee = subscription_fee;
        }

        public void setPartner_fee(String partner_fee) {
            this.partner_fee = partner_fee;
        }

        public void setPlat_manage_fee(String plat_manage_fee) {
            this.plat_manage_fee = plat_manage_fee;
        }

        public void setOther_fee(String other_fee) {
            this.other_fee = other_fee;
        }

        public void setCustom_fee_name(String custom_fee_name) {
            this.custom_fee_name = custom_fee_name;
        }

        public void setCustom_fee(String custom_fee) {
            this.custom_fee = custom_fee;
        }

        public String getDealID() {
            return dealID;
        }

        public String getDeal_name() {
            return deal_name;
        }

        public String getLimit_price() {
            return limit_price;
        }

        public String getShakes() {
            return shakes;
        }

        public String getManager_fee() {
            return manager_fee;
        }

        public String getConsult_fee() {
            return consult_fee;
        }

        public String getSubscription_fee() {
            return subscription_fee;
        }

        public String getPartner_fee() {
            return partner_fee;
        }

        public String getPlat_manage_fee() {
            return plat_manage_fee;
        }

        public String getOther_fee() {
            return other_fee;
        }

        public String getCustom_fee_name() {
            return custom_fee_name;
        }

        public String getCustom_fee() {
            return custom_fee;
        }
    }
}
