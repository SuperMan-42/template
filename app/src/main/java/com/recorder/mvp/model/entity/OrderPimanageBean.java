package com.recorder.mvp.model.entity;

import com.core.widget.recyclerview.entity.MultiItemEntity;
import com.recorder.mvp.ui.activity.BackStageManagerActivity;

import java.util.List;

/**
 * Created by hpw on 17-12-6.
 */

public class OrderPimanageBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"pi_files":[{"dealID":"f4f1b3d145ee52eb5a2300a9","deal_name":"12","files":[{"file_name":"22143613407","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/18/5a2522ff77a4d.jpg","ctime":"2017-12-04 18:27:12"},{"file_name":"WX20171129-180011","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/17/5a25177636385.png","ctime":"2017-12-04 17:37:59"}]}]}
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
         * pi_files : [{"dealID":"f4f1b3d145ee52eb5a2300a9","deal_name":"12","files":[{"file_name":"22143613407","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/18/5a2522ff77a4d.jpg","ctime":"2017-12-04 18:27:12"},{"file_name":"WX20171129-180011","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/17/5a25177636385.png","ctime":"2017-12-04 17:37:59"}]}]
         */

        private List<PiFilesEntity> pi_files;

        public void setPi_files(List<PiFilesEntity> pi_files) {
            this.pi_files = pi_files;
        }

        public List<PiFilesEntity> getPi_files() {
            return pi_files;
        }

        public static class PiFilesEntity {
            /**
             * dealID : f4f1b3d145ee52eb5a2300a9
             * deal_name : 12
             * files : [{"file_name":"22143613407","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/18/5a2522ff77a4d.jpg","ctime":"2017-12-04 18:27:12"},{"file_name":"WX20171129-180011","file":"http://ustatic-test.dreamflyc.com/attachment/201712/04/17/5a25177636385.png","ctime":"2017-12-04 17:37:59"}]
             */

            private String dealID;
            private String deal_name;
            private List<FilesEntity> files;

            public void setDealID(String dealID) {
                this.dealID = dealID;
            }

            public void setDeal_name(String deal_name) {
                this.deal_name = deal_name;
            }

            public void setFiles(List<FilesEntity> files) {
                this.files = files;
            }

            public String getDealID() {
                return dealID;
            }

            public String getDeal_name() {
                return deal_name;
            }

            public List<FilesEntity> getFiles() {
                return files;
            }

            public static class FilesEntity implements MultiItemEntity {
                /**
                 * file_name : 22143613407
                 * file : http://ustatic-test.dreamflyc.com/attachment/201712/04/18/5a2522ff77a4d.jpg
                 * ctime : 2017-12-04 18:27:12
                 */

                private boolean isFirst;
                private String file_name;
                private String file;
                private String ctime;

                public boolean getFirst() {
                    return isFirst;
                }

                public void setFirst(boolean first) {
                    isFirst = first;
                }

                public void setFile_name(String file_name) {
                    this.file_name = file_name;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public void setCtime(String ctime) {
                    this.ctime = ctime;
                }

                public String getFile_name() {
                    return file_name;
                }

                public String getFile() {
                    return file;
                }

                public String getCtime() {
                    return ctime;
                }

                @Override
                public int getItemType() {
                    return BackStageManagerActivity.ExpandableItemAdapter.TYPE_CONTENT;
                }
            }
        }
    }
}