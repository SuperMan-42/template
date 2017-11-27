package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-27.
 */

public class HomeRecommendBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"deal_recommend":{"cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779800.jpg","url":"","text":"1212"},"news_recommend":[{"newsID":"56697f0ef36ff116b433c34e","title":"昊翔资本投资安恒信息 积极布局网络安全领域","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","url":"http://www.dreamflyc.com/newsitem/278075987"},{"newsID":"05d515a14c0ff26ff116c44e","title":"","cover":"","url":""}],"zc":[{"dealID":"e4cb262a5ed06a876e07b5a8","deal_name":"","cover":"","limit_price":0,"target_fund":0,"progress":0}],"sm":[{"dealID":"32892cd8d3b3e8070c540ba9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511777573.jpg","limit_price":0,"target_fund":0,"progress":0},{"dealID":"32892cd8d3b3e8070c540ba9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511777580.jpg","limit_price":0,"target_fund":0,"progress":0},{"dealID":"abfcdcf7f8d510afd43407a9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779723.jpg","limit_price":0,"target_fund":0,"progress":0}]}
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
         * deal_recommend : {"cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779800.jpg","url":"","text":"1212"}
         * news_recommend : [{"newsID":"56697f0ef36ff116b433c34e","title":"昊翔资本投资安恒信息 积极布局网络安全领域","cover":"http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","url":"http://www.dreamflyc.com/newsitem/278075987"},{"newsID":"05d515a14c0ff26ff116c44e","title":"","cover":"","url":""}]
         * zc : [{"dealID":"e4cb262a5ed06a876e07b5a8","deal_name":"","cover":"","limit_price":0,"target_fund":0,"progress":0}]
         * sm : [{"dealID":"32892cd8d3b3e8070c540ba9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511777573.jpg","limit_price":0,"target_fund":0,"progress":0},{"dealID":"32892cd8d3b3e8070c540ba9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511777580.jpg","limit_price":0,"target_fund":0,"progress":0},{"dealID":"abfcdcf7f8d510afd43407a9","deal_name":"","cover":"http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779723.jpg","limit_price":0,"target_fund":0,"progress":0}]
         */

        private DealRecommendEntity deal_recommend;
        private List<NewsRecommendEntity> news_recommend;
        private List<ZcEntity> zc;
        private List<SmEntity> sm;

        public void setDeal_recommend(DealRecommendEntity deal_recommend) {
            this.deal_recommend = deal_recommend;
        }

        public void setNews_recommend(List<NewsRecommendEntity> news_recommend) {
            this.news_recommend = news_recommend;
        }

        public void setZc(List<ZcEntity> zc) {
            this.zc = zc;
        }

        public void setSm(List<SmEntity> sm) {
            this.sm = sm;
        }

        public DealRecommendEntity getDeal_recommend() {
            return deal_recommend;
        }

        public List<NewsRecommendEntity> getNews_recommend() {
            return news_recommend;
        }

        public List<ZcEntity> getZc() {
            return zc;
        }

        public List<SmEntity> getSm() {
            return sm;
        }

        public static class DealRecommendEntity {
            /**
             * cover : http://ustatic-test.dreamflyc.com//attachment/201711/27/181511779800.jpg
             * url :
             * text : 1212
             */

            private String cover;
            private String url;
            private String text;

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getCover() {
                return cover;
            }

            public String getUrl() {
                return url;
            }

            public String getText() {
                return text;
            }
        }

        public static class NewsRecommendEntity {
            /**
             * newsID : 56697f0ef36ff116b433c34e
             * title : 昊翔资本投资安恒信息 积极布局网络安全领域
             * cover : http://ustatic-test.dreamflyc.com/group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg
             * url : http://www.dreamflyc.com/newsitem/278075987
             */

            private String newsID;
            private String title;
            private String cover;
            private String url;

            public void setNewsID(String newsID) {
                this.newsID = newsID;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getNewsID() {
                return newsID;
            }

            public String getTitle() {
                return title;
            }

            public String getCover() {
                return cover;
            }

            public String getUrl() {
                return url;
            }
        }

        public static class Entity {
            /**
             * dealID : e4cb262a5ed06a876e07b5a8
             * deal_name :
             * cover :
             * limit_price : 0
             * target_fund : 0
             * progress : 0
             */

            private String dealID;
            private String deal_name;
            private String cover;
            private int limit_price;
            private int target_fund;
            private int progress;

            public void setDealID(String dealID) {
                this.dealID = dealID;
            }

            public void setDeal_name(String deal_name) {
                this.deal_name = deal_name;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setLimit_price(int limit_price) {
                this.limit_price = limit_price;
            }

            public void setTarget_fund(int target_fund) {
                this.target_fund = target_fund;
            }

            public void setProgress(int progress) {
                this.progress = progress;
            }

            public String getDealID() {
                return dealID;
            }

            public String getDeal_name() {
                return deal_name;
            }

            public String getCover() {
                return cover;
            }

            public int getLimit_price() {
                return limit_price;
            }

            public int getTarget_fund() {
                return target_fund;
            }

            public int getProgress() {
                return progress;
            }
        }

        static class SmEntity extends Entity {
        }

        public static class ZcEntity extends Entity {
        }
    }
}
