package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-27.
 */

public class ImageUploadBean {

    /**
     * errno : 0
     * error : 成功
     * data : {"images":["group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY037.jpg"]}
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
         * images : ["group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY036.jpg","group1/M00/02/37/ChIGqldLyNuATr6zAADXlrJMiTY037.jpg"]
         */

        private List<String> images;

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
