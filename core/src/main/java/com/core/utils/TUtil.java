package com.core.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by hpw on 16/10/28.
 */

public class TUtil {
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void setImage(Context mContext, Object url, ImageView view) {
//        setImage(mContext, url, view, false);
//    }
//
//    public static void setImage(Context mContext, Object url, int place, ImageView view) {
//        setImage(mContext, url, place, view, false);
//    }
//
//    public static void setImage(Context mContext, Object url, ImageView view, Boolean isCircle) {
//        if (isCircle != true)
//            Glide.with(mContext).load(url).crossFade().placeholder(R.drawable.ysh_brand).into(view);
//        else
//            Glide.with(mContext).load(url).crossFade().placeholder(R.drawable.ysh_brand).transform(new GlideCircleTransform(mContext)).into(view);
//    }
//
//    public static void setImage(Context mContext, Object url, int place, ImageView view, Boolean isCircle) {
//        if (isCircle != true)
//            Glide.with(mContext).load(url).crossFade().placeholder(place).into(view);
//        else
//            Glide.with(mContext).load(url).crossFade().placeholder(place).transform(new GlideCircleTransform(mContext)).into(view);
//    }
}

