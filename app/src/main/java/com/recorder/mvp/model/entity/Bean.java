package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-11-22.
 */

public class Bean {
    String key;
    String value;

    public Bean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
