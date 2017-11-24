package com.recorder.mvp.contract;

import com.core.http.imageloader.ImageLoader;
import com.core.mvp.IModel;
import com.core.mvp.IView;

import io.reactivex.Observable;

public interface RegisterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showRegisterSuccess(ImageLoader imageLoader, Object object);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Object> registerUser(String mobile, String password, String code);
    }
}
