package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-27.
 */

public class DealFilter {

    /**
     * errno : 0
     * error : 成功
     * data : {"labels":[{"id":"1","name":"金融"},{"id":"16","name":"农业"},{"id":"17","name":"互联网"},{"id":"18","name":"游戏动漫"},{"id":"19","name":"广告营销"},{"id":"20","name":"共享经济"},{"id":"21","name":"智能机械"},{"id":"22","name":"AR-VR"},{"id":"23","name":"移动互联网"},{"id":"24","name":"互联网金融"},{"id":"25","name":"工业"},{"id":"26","name":"能源"},{"id":"15","name":"体育产业"},{"id":"14","name":"人工智能"},{"id":"13","name":"物流运输"},{"id":"2","name":"教育培训"},{"id":"3","name":"电子商务"},{"id":"4","name":"社交"},{"id":"5","name":"硬件设备"},{"id":"6","name":"文娱传媒"},{"id":"7","name":"消费生活"},{"id":"8","name":"医疗健康"},{"id":"9","name":"企业服务"},{"id":"10","name":"旅游酒店"},{"id":"11","name":"房产家居"},{"id":"12","name":"汽车交通"},{"id":"27","name":"高新技术"}],"rounds":[{"id":1,"name":"天使轮"},{"id":2,"name":"Pre-A轮"},{"id":3,"name":"A轮"},{"id":4,"name":"B轮"},{"id":5,"name":"C轮"},{"id":6,"name":"D轮"},{"id":7,"name":"E轮"},{"id":8,"name":"Pre-IPO"},{"id":9,"name":"IPO"},{"id":10,"name":"未公开"}]}
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
         * labels : [{"id":"1","name":"金融"},{"id":"16","name":"农业"},{"id":"17","name":"互联网"},{"id":"18","name":"游戏动漫"},{"id":"19","name":"广告营销"},{"id":"20","name":"共享经济"},{"id":"21","name":"智能机械"},{"id":"22","name":"AR-VR"},{"id":"23","name":"移动互联网"},{"id":"24","name":"互联网金融"},{"id":"25","name":"工业"},{"id":"26","name":"能源"},{"id":"15","name":"体育产业"},{"id":"14","name":"人工智能"},{"id":"13","name":"物流运输"},{"id":"2","name":"教育培训"},{"id":"3","name":"电子商务"},{"id":"4","name":"社交"},{"id":"5","name":"硬件设备"},{"id":"6","name":"文娱传媒"},{"id":"7","name":"消费生活"},{"id":"8","name":"医疗健康"},{"id":"9","name":"企业服务"},{"id":"10","name":"旅游酒店"},{"id":"11","name":"房产家居"},{"id":"12","name":"汽车交通"},{"id":"27","name":"高新技术"}]
         * rounds : [{"id":1,"name":"天使轮"},{"id":2,"name":"Pre-A轮"},{"id":3,"name":"A轮"},{"id":4,"name":"B轮"},{"id":5,"name":"C轮"},{"id":6,"name":"D轮"},{"id":7,"name":"E轮"},{"id":8,"name":"Pre-IPO"},{"id":9,"name":"IPO"},{"id":10,"name":"未公开"}]
         */

        private List<LabelsEntity> labels;
        private List<RoundsEntity> rounds;

        public void setLabels(List<LabelsEntity> labels) {
            this.labels = labels;
        }

        public void setRounds(List<RoundsEntity> rounds) {
            this.rounds = rounds;
        }

        public List<LabelsEntity> getLabels() {
            return labels;
        }

        public List<RoundsEntity> getRounds() {
            return rounds;
        }

        public static class LabelsEntity {
            /**
             * id : 1
             * name : 金融
             */

            private String id;
            private String name;

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        public static class RoundsEntity {
            /**
             * id : 1
             * name : 天使轮
             */

            private int id;
            private String name;

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}
