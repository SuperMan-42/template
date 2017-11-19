package com.recorder;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2017/11/19.
 */

public class YshNameValuePair extends BasicNameValuePair implements Comparable<YshNameValuePair> {

    public YshNameValuePair(String name, String value) {
        super(name, value);
    }

    @Override
    public int compareTo(YshNameValuePair another) {
        return getName().compareTo(another.getName());
    }
}
