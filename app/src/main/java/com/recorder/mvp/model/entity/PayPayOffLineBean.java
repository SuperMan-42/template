package com.recorder.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by hpw on 17-12-7.
 */

public class PayPayOffLineBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"payee":"dreamflyc","bank_name":"招商银行北京支行上地分行","account":"62839049039302903","bank_memo":"投资款"}
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

    public static class DataEntity implements Serializable {
        private static final long serialVersionUID = -6373808465331320979L;
        /**
         * payee : dreamflyc
         * bank_name : 招商银行北京支行上地分行
         * account : 62839049039302903
         * bank_memo : 投资款
         */

        private String payee;
        private String bank_name;
        private String account;
        private String bank_memo;

        public void setPayee(String payee) {
            this.payee = payee;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setBank_memo(String bank_memo) {
            this.bank_memo = bank_memo;
        }

        public String getPayee() {
            return payee;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getAccount() {
            return account;
        }

        public String getBank_memo() {
            return bank_memo;
        }
    }
}
