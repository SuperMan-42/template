package com.recorder.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpw on 17-12-4.
 */

public class PayCheckBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"dealID":"6f10e0b2e9070c5484380ca9","deal_name":"长篇史诗电视剧<<利顺德传奇>>","limit_price":"5","shakes":"0.006","manager_fee":"1","manager_fee_year":"2","consult_fee":"0","consult_fee_year":"0","subscription_fee":"0","subscription_fee_year":"0","partner_fee":"0","partner_fee_year":"0","plat_manage_fee":"0","plat_manage_fee_year":"0","other_fee":"0","other_fee_year":"0","custom_fee_name":"","custom_fee":"0","custom_fee_year":"0","purchse_agreement":[{"file_name":"投资顾问协议协议协议","file":"http://ustatic-test.dreamflyc.com//attachment/201712/11/15/5a2e3ac9f2337.pdf"}]}
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
         * dealID : 6f10e0b2e9070c5484380ca9
         * deal_name : 长篇史诗电视剧<<利顺德传奇>>
         * limit_price : 5
         * shakes : 0.006
         * manager_fee : 1
         * manager_fee_year : 2
         * consult_fee : 0
         * consult_fee_year : 0
         * subscription_fee : 0
         * subscription_fee_year : 0
         * partner_fee : 0
         * partner_fee_year : 0
         * plat_manage_fee : 0
         * plat_manage_fee_year : 0
         * other_fee : 0
         * other_fee_year : 0
         * custom_fee_name :
         * custom_fee : 0
         * custom_fee_year : 0
         * purchse_agreement : [{"file_name":"投资顾问协议协议协议","file":"http://ustatic-test.dreamflyc.com//attachment/201712/11/15/5a2e3ac9f2337.pdf"}]
         */

        private String dealID;
        private String deal_name;
        private String limit_price;
        private String shakes;
        private String manager_fee;
        private String manager_fee_year;
        private String consult_fee;
        private String consult_fee_year;
        private String subscription_fee;
        private String subscription_fee_year;
        private String partner_fee;
        private String partner_fee_year;
        private String plat_manage_fee;
        private String plat_manage_fee_year;
        private String other_fee;
        private String other_fee_year;
        private String custom_fee_name;
        private String custom_fee;
        private String custom_fee_year;
        private List<PurchseAgreementEntity> purchse_agreement;

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

        public void setManager_fee_year(String manager_fee_year) {
            this.manager_fee_year = manager_fee_year;
        }

        public void setConsult_fee(String consult_fee) {
            this.consult_fee = consult_fee;
        }

        public void setConsult_fee_year(String consult_fee_year) {
            this.consult_fee_year = consult_fee_year;
        }

        public void setSubscription_fee(String subscription_fee) {
            this.subscription_fee = subscription_fee;
        }

        public void setSubscription_fee_year(String subscription_fee_year) {
            this.subscription_fee_year = subscription_fee_year;
        }

        public void setPartner_fee(String partner_fee) {
            this.partner_fee = partner_fee;
        }

        public void setPartner_fee_year(String partner_fee_year) {
            this.partner_fee_year = partner_fee_year;
        }

        public void setPlat_manage_fee(String plat_manage_fee) {
            this.plat_manage_fee = plat_manage_fee;
        }

        public void setPlat_manage_fee_year(String plat_manage_fee_year) {
            this.plat_manage_fee_year = plat_manage_fee_year;
        }

        public void setOther_fee(String other_fee) {
            this.other_fee = other_fee;
        }

        public void setOther_fee_year(String other_fee_year) {
            this.other_fee_year = other_fee_year;
        }

        public void setCustom_fee_name(String custom_fee_name) {
            this.custom_fee_name = custom_fee_name;
        }

        public void setCustom_fee(String custom_fee) {
            this.custom_fee = custom_fee;
        }

        public void setCustom_fee_year(String custom_fee_year) {
            this.custom_fee_year = custom_fee_year;
        }

        public void setPurchse_agreement(List<PurchseAgreementEntity> purchse_agreement) {
            this.purchse_agreement = purchse_agreement;
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

        public String getManager_fee_year() {
            return manager_fee_year;
        }

        public String getConsult_fee() {
            return consult_fee;
        }

        public String getConsult_fee_year() {
            return consult_fee_year;
        }

        public String getSubscription_fee() {
            return subscription_fee;
        }

        public String getSubscription_fee_year() {
            return subscription_fee_year;
        }

        public String getPartner_fee() {
            return partner_fee;
        }

        public String getPartner_fee_year() {
            return partner_fee_year;
        }

        public String getPlat_manage_fee() {
            return plat_manage_fee;
        }

        public String getPlat_manage_fee_year() {
            return plat_manage_fee_year;
        }

        public String getOther_fee() {
            return other_fee;
        }

        public String getOther_fee_year() {
            return other_fee_year;
        }

        public String getCustom_fee_name() {
            return custom_fee_name;
        }

        public String getCustom_fee() {
            return custom_fee;
        }

        public String getCustom_fee_year() {
            return custom_fee_year;
        }

        public List<PurchseAgreementEntity> getPurchse_agreement() {
            return purchse_agreement;
        }

        public static class PurchseAgreementEntity implements Serializable {
            /**
             * file_name : 投资顾问协议协议协议
             * file : http://ustatic-test.dreamflyc.com//attachment/201712/11/15/5a2e3ac9f2337.pdf
             */

            private String file_name;
            private String file;
            private boolean isCheck = false;

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getFile_name() {
                return file_name;
            }

            public String getFile() {
                return file;
            }

            public boolean getCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }
}
