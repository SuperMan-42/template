package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-23.
 */

public class EquityBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"list":[{"dealID":"6f10e0b2e9070c5484380ca9","deal_name":"网络电影《人间大炮3》","brief":"当红主播与本山弟子打造东北风格爆笑喜剧电影","cover":"","round":"","labels":["文娱传媒"],"is_group":"0","online_str":"","view_footer":{"view":0,"focus":0,"consult":0}}],"page":1,"page_size":20,"total_count":1,"total_page":1}
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
         * list : [{"dealID":"6f10e0b2e9070c5484380ca9","deal_name":"网络电影《人间大炮3》","brief":"当红主播与本山弟子打造东北风格爆笑喜剧电影","cover":"","round":"","labels":["文娱传媒"],"is_group":"0","online_str":"","view_footer":{"view":0,"focus":0,"consult":0}}]
         * page : 1
         * page_size : 20
         * total_count : 1
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
             * dealID : 6f10e0b2e9070c5484380ca9
             * deal_name : 网络电影《人间大炮3》
             * brief : 当红主播与本山弟子打造东北风格爆笑喜剧电影
             * cover :
             * round :
             * labels : ["文娱传媒"]
             * is_group : 0
             * online_str :
             * view_footer : {"view":0,"focus":0,"consult":0}
             */

            private String dealID;
            private String deal_name;
            private String brief;
            private String cover;
            private String round;
            private String is_group;
            private String online_str;
            private ViewFooterEntity view_footer;
            private List<String> labels;

            public void setDealID(String dealID) {
                this.dealID = dealID;
            }

            public void setDeal_name(String deal_name) {
                this.deal_name = deal_name;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setRound(String round) {
                this.round = round;
            }

            public void setIs_group(String is_group) {
                this.is_group = is_group;
            }

            public void setOnline_str(String online_str) {
                this.online_str = online_str;
            }

            public void setView_footer(ViewFooterEntity view_footer) {
                this.view_footer = view_footer;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }

            public String getDealID() {
                return dealID;
            }

            public String getDeal_name() {
                return deal_name;
            }

            public String getBrief() {
                return brief;
            }

            public String getCover() {
                return cover;
            }

            public String getRound() {
                return round;
            }

            public String getIs_group() {
                return is_group;
            }

            public String getOnline_str() {
                return online_str;
            }

            public ViewFooterEntity getView_footer() {
                return view_footer;
            }

            public List<String> getLabels() {
                return labels;
            }

            public static class ViewFooterEntity {
                /**
                 * view : 0
                 * focus : 0
                 * consult : 0
                 */

                private int view;
                private int focus;
                private int consult;

                public void setView(int view) {
                    this.view = view;
                }

                public void setFocus(int focus) {
                    this.focus = focus;
                }

                public void setConsult(int consult) {
                    this.consult = consult;
                }

                public int getView() {
                    return view;
                }

                public int getFocus() {
                    return focus;
                }

                public int getConsult() {
                    return consult;
                }
            }
        }
    }
}
