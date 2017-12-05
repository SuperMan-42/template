package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-12-4.
 */

public class PayPayBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"payment_sn":"2017120111511877620000930","msg":"创建订单成功","member_user_id":"M200000550","sign":"MhrqrZdEn+hzCnm+z0kW/MH/IYeioyAZ7QS/rhg+xY8bNb19xwAl4QusMI1SBjbCJJs1KjTXNPcn5F6qyJ6qeIusWLYSf+zBu8GFd+iqo35PszuIwR+rBX6H+lL+mXxMe2mmuLdf0bYk67tU69KwFoinBWTEGy8NCrWMMG1kYHk=","merchantId":"M200000550"}
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
         * payment_sn : 2017120111511877620000930
         * msg : 创建订单成功
         * member_user_id : M200000550
         * sign : MhrqrZdEn+hzCnm+z0kW/MH/IYeioyAZ7QS/rhg+xY8bNb19xwAl4QusMI1SBjbCJJs1KjTXNPcn5F6qyJ6qeIusWLYSf+zBu8GFd+iqo35PszuIwR+rBX6H+lL+mXxMe2mmuLdf0bYk67tU69KwFoinBWTEGy8NCrWMMG1kYHk=
         * merchantId : M200000550
         */

        private String payment_sn;
        private String msg;
        private String member_user_id;
        private String sign;
        private String merchantId;

        public void setPayment_sn(String payment_sn) {
            this.payment_sn = payment_sn;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setMember_user_id(String member_user_id) {
            this.member_user_id = member_user_id;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getPayment_sn() {
            return payment_sn;
        }

        public String getMsg() {
            return msg;
        }

        public String getMember_user_id() {
            return member_user_id;
        }

        public String getSign() {
            return sign;
        }

        public String getMerchantId() {
            return merchantId;
        }
    }
}
