package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-12-5.
 */

public class OrderListBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"list":[{"orderID":"c0c93dbf2639c6dffe336b43","order_sn":"17113012470400000349231","deal_name":"网络电影《人间大炮3》2","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","amount":"100000","manager_amount":"5000","consult_amount":"0","subscription_amount":"0","partner_amount":"0","plat_manage_amount":"0","other_amount":"0","custom_amount":"0","custom_amount_name":"","actual_amount":"105000","order_status":"0","order_status_name":"待支付"},{"orderID":"28888a011abe3339c6df6a43","order_sn":"17113012400800000398713","deal_name":"网络电影《人间大炮3》2","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","amount":"100000","manager_amount":"5000","consult_amount":"0","subscription_amount":"0","partner_amount":"0","plat_manage_amount":"0","other_amount":"0","custom_amount":"0","custom_amount_name":"","actual_amount":"105000","order_status":"4","order_status_name":"待线下打款"}],"page":1,"page_size":20,"total_count":2,"total_page":1}
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
         * list : [{"orderID":"c0c93dbf2639c6dffe336b43","order_sn":"17113012470400000349231","deal_name":"网络电影《人间大炮3》2","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","amount":"100000","manager_amount":"5000","consult_amount":"0","subscription_amount":"0","partner_amount":"0","plat_manage_amount":"0","other_amount":"0","custom_amount":"0","custom_amount_name":"","actual_amount":"105000","order_status":"0","order_status_name":"待支付"},{"orderID":"28888a011abe3339c6df6a43","order_sn":"17113012400800000398713","deal_name":"网络电影《人间大炮3》2","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","amount":"100000","manager_amount":"5000","consult_amount":"0","subscription_amount":"0","partner_amount":"0","plat_manage_amount":"0","other_amount":"0","custom_amount":"0","custom_amount_name":"","actual_amount":"105000","order_status":"4","order_status_name":"待线下打款"}]
         * page : 1
         * page_size : 20
         * total_count : 2
         * total_page : 1
         */

        private int page;
        private int page_size;
        private int total_count;
        private int total_page;
        private List<ListEntity> list;

        public void setPage(int page) {
            this.page = page;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public int getPage() {
            return page;
        }

        public int getPage_size() {
            return page_size;
        }

        public int getTotal_count() {
            return total_count;
        }

        public int getTotal_page() {
            return total_page;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            /**
             * orderID : c0c93dbf2639c6dffe336b43
             * order_sn : 17113012470400000349231
             * deal_name : 网络电影《人间大炮3》2
             * cover : http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg
             * amount : 100000
             * manager_amount : 5000
             * consult_amount : 0
             * subscription_amount : 0
             * partner_amount : 0
             * plat_manage_amount : 0
             * other_amount : 0
             * custom_amount : 0
             * custom_amount_name :
             * actual_amount : 105000
             * order_status : 0
             * order_status_name : 待支付
             */

            private String orderID;
            private String order_sn;
            private String deal_name;
            private String cover;
            private String amount;
            private String manager_amount;
            private String consult_amount;
            private String subscription_amount;
            private String partner_amount;
            private String plat_manage_amount;
            private String other_amount;
            private String custom_amount;
            private String custom_amount_name;
            private String actual_amount;
            private int order_status;
            private String order_status_name;

            public void setOrderID(String orderID) {
                this.orderID = orderID;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public void setDeal_name(String deal_name) {
                this.deal_name = deal_name;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public void setManager_amount(String manager_amount) {
                this.manager_amount = manager_amount;
            }

            public void setConsult_amount(String consult_amount) {
                this.consult_amount = consult_amount;
            }

            public void setSubscription_amount(String subscription_amount) {
                this.subscription_amount = subscription_amount;
            }

            public void setPartner_amount(String partner_amount) {
                this.partner_amount = partner_amount;
            }

            public void setPlat_manage_amount(String plat_manage_amount) {
                this.plat_manage_amount = plat_manage_amount;
            }

            public void setOther_amount(String other_amount) {
                this.other_amount = other_amount;
            }

            public void setCustom_amount(String custom_amount) {
                this.custom_amount = custom_amount;
            }

            public void setCustom_amount_name(String custom_amount_name) {
                this.custom_amount_name = custom_amount_name;
            }

            public void setActual_amount(String actual_amount) {
                this.actual_amount = actual_amount;
            }

            public void setOrder_status(int order_status) {
                this.order_status = order_status;
            }

            public void setOrder_status_name(String order_status_name) {
                this.order_status_name = order_status_name;
            }

            public String getOrderID() {
                return orderID;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public String getDeal_name() {
                return deal_name;
            }

            public String getCover() {
                return cover;
            }

            public String getAmount() {
                return amount;
            }

            public String getManager_amount() {
                return manager_amount;
            }

            public String getConsult_amount() {
                return consult_amount;
            }

            public String getSubscription_amount() {
                return subscription_amount;
            }

            public String getPartner_amount() {
                return partner_amount;
            }

            public String getPlat_manage_amount() {
                return plat_manage_amount;
            }

            public String getOther_amount() {
                return other_amount;
            }

            public String getCustom_amount() {
                return custom_amount;
            }

            public String getCustom_amount_name() {
                return custom_amount_name;
            }

            public String getActual_amount() {
                return actual_amount;
            }

            public int getOrder_status() {
                return order_status;
            }

            public String getOrder_status_name() {
                return order_status_name;
            }
        }
    }
}
