package com.hooks.entities;

public class FingerprintItem {

    public String type;
    public String name;
    public String value;
    public String newValue;
    public boolean enable;

    public FingerprintItem(String type, String name, String value, String newValue, boolean enable){
        this.type = type;
        this.name = name;
        this.value = value;
        this.newValue = newValue;
        this.enable = enable;
    }
}
