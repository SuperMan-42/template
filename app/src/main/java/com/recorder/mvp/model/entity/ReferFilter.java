package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-18.
 */

public class ReferFilter {

    /**
     * errno : 0
     * error :
     * data : {"trade":["教育","企业服务","消费升级","电子商务","旅游","社交网络","金融","生活服务","体育运动","O2O","健康医疗","硬件","游戏动漫","汽车交通","农业","房产家居","快递物流","广告营销","媒体娱乐","其他","工业4.0","新能源新材料","智能硬件","人工智能","军工航天","物联网","保险","招聘","工具"],"rounds":["天使轮","Pre-A轮","A轮","B轮","C轮","D轮","E轮","Pre-IPO轮","未公开"]}
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
         * trade : ["教育","企业服务","消费升级","电子商务","旅游","社交网络","金融","生活服务","体育运动","O2O","健康医疗","硬件","游戏动漫","汽车交通","农业","房产家居","快递物流","广告营销","媒体娱乐","其他","工业4.0","新能源新材料","智能硬件","人工智能","军工航天","物联网","保险","招聘","工具"]
         * rounds : ["天使轮","Pre-A轮","A轮","B轮","C轮","D轮","E轮","Pre-IPO轮","未公开"]
         */

        private List<String> trade;
        private List<String> rounds;

        public void setTrade(List<String> trade) {
            this.trade = trade;
        }

        public void setRounds(List<String> rounds) {
            this.rounds = rounds;
        }

        public List<String> getTrade() {
            return trade;
        }

        public List<String> getRounds() {
            return rounds;
        }
    }
}
