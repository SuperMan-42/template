package com.recorder.mvp.model.entity;

/**
 * Created by hpw on 17-11-22.
 */

public class Bean {
    String key;
    String value;
    String other;

    public Bean(String key, String value, String other) {
        this.key = key;
        this.value = value;
        this.other = other;
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

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
