package com.recorder.mvp.contract;

import com.core.mvp.IModel;
import com.core.mvp.IView;
import com.recorder.mvp.model.entity.LoginBean;

import io.reactivex.Observable;

public interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showLoginSuccess(LoginBean loginBean);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<LoginBean> login(String mobile, String password);
    }
}
