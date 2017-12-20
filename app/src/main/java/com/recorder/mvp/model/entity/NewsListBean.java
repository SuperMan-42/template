package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-27.
 */

public class NewsListBean {
    /**
     * errno : 0
     * error : 成功
     * data : {"list":[{"newsID":"3cc6c16ef316b4330407c24e","title":"般若云资本与海尔金控签署战略合作协议","brief":"9月19日，北京般若云资本管理有限公司（以下简称\u201c般若云资本\u201d）与海尔集团（青岛）金融控股有限公司（以下简称\u201c海尔金控\u201d）签署战略合作协议，般若云资本董事长王贵亚和海尔集团执行副总裁谭丽霞出席签约仪式。","url":"http://www.dreamflyc.com/newsitem/278075950","cover":""},{"newsID":"56697f0ef36ff116b433c34e","title":"般若云资本投资安恒信息 积极布局网络安全领域","brief":"9月19日，北京般若云资本管理有限公司（以下简称\u201c般若云资本\u201d）与海尔集团（青岛）金融控股有限公司（以下简称\u201c海尔金控\u201d）签署战略合作协议，般若云资本董事长王贵亚和海尔集团执行副总裁谭丽霞出席签约仪式。","url":"http://www.dreamflyc.com/newsitem/278075987","cover":""}],"page":1,"page_size":20,"total_count":2,"total_page":1}
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
         * list : [{"newsID":"3cc6c16ef316b4330407c24e","title":"般若云资本与海尔金控签署战略合作协议","brief":"9月19日，北京般若云资本管理有限公司（以下简称\u201c般若云资本\u201d）与海尔集团（青岛）金融控股有限公司（以下简称\u201c海尔金控\u201d）签署战略合作协议，般若云资本董事长王贵亚和海尔集团执行副总裁谭丽霞出席签约仪式。","url":"http://www.dreamflyc.com/newsitem/278075950","cover":""},{"newsID":"56697f0ef36ff116b433c34e","title":"般若云资本投资安恒信息 积极布局网络安全领域","brief":"9月19日，北京般若云资本管理有限公司（以下简称\u201c般若云资本\u201d）与海尔集团（青岛）金融控股有限公司（以下简称\u201c海尔金控\u201d）签署战略合作协议，般若云资本董事长王贵亚和海尔集团执行副总裁谭丽霞出席签约仪式。","url":"http://www.dreamflyc.com/newsitem/278075987","cover":""}]
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
             * newsID : 3cc6c16ef316b4330407c24e
             * title : 般若云资本与海尔金控签署战略合作协议
             * brief : 9月19日，北京般若云资本管理有限公司（以下简称“般若云资本”）与海尔集团（青岛）金融控股有限公司（以下简称“海尔金控”）签署战略合作协议，般若云资本董事长王贵亚和海尔集团执行副总裁谭丽霞出席签约仪式。
             * url : http://www.dreamflyc.com/newsitem/278075950
             * cover :
             */

            private String newsID;
            private String title;
            private String brief;
            private String url;
            private String cover;

            public void setNewsID(String newsID) {
                this.newsID = newsID;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getNewsID() {
                return newsID;
            }

            public String getTitle() {
                return title;
            }

            public String getBrief() {
                return brief;
            }

            public String getUrl() {
                return url;
            }

            public String getCover() {
                return cover;
            }
        }
    }
}
