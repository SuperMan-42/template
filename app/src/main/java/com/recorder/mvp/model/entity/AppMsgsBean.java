package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-12-12.
 */

public class AppMsgsBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"list":[{"content":"项目已上线沙发斯蒂芬"},{"content":"北京哈哈哈北京哈哈哈北京哈哈哈北京哈哈哈"},{"content":"sasdfasdfasdfasdfasdf"},{"content":"aa"},{"content":"adfadsf"},{"content":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},{"content":"你好，这是asdfasdf"}],"page":1,"page_size":20,"total_count":7,"total_page":1}
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
         * list : [{"content":"项目已上线沙发斯蒂芬"},{"content":"北京哈哈哈北京哈哈哈北京哈哈哈北京哈哈哈"},{"content":"sasdfasdfasdfasdfasdf"},{"content":"aa"},{"content":"adfadsf"},{"content":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},{"content":"你好，这是asdfasdf"}]
         * page : 1
         * page_size : 20
         * total_count : 7
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
             * content : 项目已上线沙发斯蒂芬
             */

            private String content;
            private String ctime;

            public void setContent(String content) {
                this.content = content;
            }

            public String getContent() {
                return content;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }
        }
    }
}
